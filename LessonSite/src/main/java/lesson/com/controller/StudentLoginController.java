package lesson.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.AdminEntity;
import lesson.com.model.entity.StudentEntity;
import lesson.com.model.entity.UuIdEntity;
import lesson.com.service.StudentService;
import lesson.com.service.UuIdService;

@Controller
@RequestMapping("/user")
public class StudentLoginController {
	@Autowired
	StudentService studentService;

	@Autowired
	UuIdService uuIdService;

	@Autowired
	HttpSession session;
	@Autowired
	ServletContext servletContext;
	@Autowired
	MailSender mailSender;

	@GetMapping("/login")
	public String getStudentLoginPage() {
		//user_login.htmlが表示される。
		return "user_login.html";
	}
	@PostMapping("/login")
	public String studentAuth(@RequestParam String studentEmail,@RequestParam String studentPassword,Model model) {
		//studentServiceクラスのfindByStudentEmailAndPasswordメソッドを使用して、該当するユーザー情報を取得する。
		StudentEntity studentEntity = studentService.findByStudentEmailAndPassword(studentEmail, studentPassword);
		//adminEntity　== null
		if(studentEntity == null) {
			//user_login.htmlが表示される。
			return "user_login.html";
		}else {
			//studentEntityの内容をsessionに保存する
			session.setAttribute("student",studentEntity);
			//現在ログインしている生徒情報を取得する
			StudentEntity student = (StudentEntity) session.getAttribute("student");
			//現在ログインしている人の名前を取得する
			String loginStudentName =student.getStudentName();
			model.addAttribute("loginStudentName",loginStudentName);
			String url = (String) session.getAttribute("goLogin");
			if(url == null) {
				return "redirect:/lesson/menu";
			}else {
				return "redirect:"+url;
			}
			
		}
	}
	@GetMapping("/password/reset")
	public String passwordResetPage() {
		return "user_password_reset.html";
	}

	@GetMapping("/password/change/{uuIdMail}")
	public String passwordResetPage(@PathVariable String uuIdMail,Model model) {
		UuIdEntity userEmail = uuIdService.getByUuId(uuIdMail);
		model.addAttribute("userEmail",userEmail);
		return "user_password_change.html";
	}
	
	@PostMapping("/change/password/complete")
	public String getChangePasswordComplete(@RequestParam String userEmail,@RequestParam String password,Model model) {
		studentService.updateAccount(userEmail, password);
		return "user_change_password_complete.html";
	
	}
	
	

	@PostMapping("/password/reset/mail")
	public String getResetPasswordMail(@RequestParam String userEmail,Model model) {
		String uuIdMail = uuIdService.createUUID(userEmail);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
		msg.setTo(userEmail); // 送信先メールアドレス
		msg.setSubject("パスワード変更メール"); // タイトル               
		msg.setText("以下のリンクを1時間以内にクリックして下さい"+"http://localhost:8080/user/password/change/"+uuIdMail); //本文
		try {
			mailSender.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
		}	
        model.addAttribute("userEmail",userEmail);
		return "user_fix_send_mail.html";

	}
	
	@GetMapping("/logout")
	public String userLogout(RedirectAttributes redirectAttributes) {
		
		session.invalidate();
		redirectAttributes.addAttribute("logoutMessage", "ログアウトしました");
		return "redirect:/lesson/menu/logout";
	}

}
