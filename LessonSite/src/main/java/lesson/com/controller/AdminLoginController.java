package lesson.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.AdminEntity;
import lesson.com.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {
	@Autowired
	AdminService adminService;

	@Autowired
	HttpSession session;

	@GetMapping("/login")
	public String getAdminLoginPage() {
		//admin_login.htmlが表示される。
		return "admin_login.html";
	}
	@PostMapping("/login")
	public String adminAuth(@RequestParam String adminEmail,@RequestParam String adminPassword,Model model) {
		//adminServiceクラスのfindByAdminEmailAndPasswordメソッドを使用して、該当するユーザー情報を取得する。
		AdminEntity adminEntity = adminService.findByAdminEmailAndPassword(adminEmail, adminPassword);
		//adminEntity　== null
		if(adminEntity == null) {
			//admin_login.htmlが表示される。
			return "admin_login.html";
		}else {
			//adminEntityの内容をsessionに保存する
			session.setAttribute("admin",adminEntity);
			return "redirect:/admin/lesson/all";
		}
	}
	
	@GetMapping("/logout")
	public String adminLogout() {
		
		session.invalidate();
		return "redirect:/admin/login";
	}
}
