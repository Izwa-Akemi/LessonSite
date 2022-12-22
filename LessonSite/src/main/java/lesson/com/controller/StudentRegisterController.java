package lesson.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lesson.com.service.StudentService;

@Controller
@RequestMapping("/user")
public class StudentRegisterController {
	@Autowired
    private StudentService studentService;
	
	//新規登録画面を表示
    @GetMapping("/register")
    public String getStudentRegisterPage() {
        return "user_register.html";
    }
    
  //登録内容を保存
    @PostMapping("/confirm")
    public String confirm(@RequestParam String studentName,@RequestParam String studentEmail,@RequestParam String studentPassword,Model model) {
        model.addAttribute("studentName",studentName);
        model.addAttribute("studentEmail",studentEmail);
        model.addAttribute("studentPassword",studentPassword);
        return "user_confirm_register.html";
    }
    //登録内容を保存
    @PostMapping("/register")
    public String register(@RequestParam String studentName,@RequestParam String studentEmail,@RequestParam String studentPassword) {
    	studentService.createAccount(studentName, studentEmail, studentPassword);
        return "redirect:/user/login";
    }
}
