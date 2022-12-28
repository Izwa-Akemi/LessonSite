package lesson.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.LessonEntity;
import lesson.com.service.LessonService;

@Controller
@RequestMapping("/lesson")
public class StudentLessonController {
	@Autowired
	LessonService lessonService;
	@Autowired
	HttpSession session;
	
	@GetMapping("/menu")
	public String getLessonMenuPage(Model model) {
		List<LessonEntity>lessonList = lessonService.findAllLesson();
		model.addAttribute("lessonList",lessonList);
		return "user_menu.html";
	}
	@GetMapping("/detail/{lessonId}")
	public String getLessonDetailPage(@PathVariable Long lessonId, Model model) {
		LessonEntity lesson  = lessonService.findByLessonId(lessonId);
		model.addAttribute("lesson",lesson);
		return "user_lesson_detail.html";
	}
}
