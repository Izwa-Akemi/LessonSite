package lesson.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.AdminEntity;
import lesson.com.model.entity.StudentEntity;
import lesson.com.service.StudentService;

@Controller
@RequestMapping("/user")
public class StudentLoginController {
	@Autowired
	StudentService studentService;
	@Autowired
	HttpSession session;

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
			return "user_menu.html";
		}
	}
}
