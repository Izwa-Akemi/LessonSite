package lesson.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lesson.com.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminRegisterController {
	@Autowired
    private AdminService adminService;
    //新規登録画面を表示
    @GetMapping("/register")
    public String getAdminRegisterPage() {
        return "admin_register.html";
    }
    //登録内容を保存
    @PostMapping("/confirm")
    public String confirm(@RequestParam String adminName,@RequestParam String adminEmail,@RequestParam String adminPassword,Model model) {
        model.addAttribute("adminName",adminName);
        model.addAttribute("adminEmail",adminEmail);
        model.addAttribute("adminPassword",adminPassword);
        return "admin_confirm_register.html";
    }
    //登録内容を保存
    @PostMapping("/register")
    public String register(@RequestParam String adminName,@RequestParam String adminEmail,@RequestParam String adminPassword) {
        adminService.createAccount(adminName, adminEmail, adminPassword);
        return "redirect:/admin/login";
    }
  
}
