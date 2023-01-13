package lesson.com.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		List<LessonEntity>lessonList = lessonService.findActiveAllLesson();
		model.addAttribute("lessonList",lessonList);
		return "user_menu.html";
	}
	@GetMapping("/detail/{lessonId}")
	public String getLessonDetailPage(@PathVariable Long lessonId, Model model) {
		LessonEntity lesson  = lessonService.findByLessonId(lessonId);
		model.addAttribute("lesson",lesson);
		return "user_lesson_detail.html";
	}
	public boolean isLessonExist(Long lessonId, LinkedList<LessonEntity> list){
        Iterator<LessonEntity> ite = list.iterator();
        boolean isExist = false;
        while(ite.hasNext()){
            LessonEntity lesson = ite.next();
            if(lesson.getLessonId() == lessonId){
                isExist = true;
                break;
            }
        }
        return isExist;

    }

	@PostMapping("/cart/all")
	public String addCartPage(@RequestParam Long lessonId,Model model) {
		if(session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list  = new LinkedList<LessonEntity>();
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			list.add(lessons);
			session.setAttribute("cart", list);
			model.addAttribute("list",list);
			return "user_planned_application.html";
		}else {
			
			session.getAttribute("cart");
			LinkedList<LessonEntity> list  = new LinkedList<LessonEntity>();
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			if(isLessonExist(lessonId, list)) {
				
			}else {
				list.add(lessons);	
			}
			model.addAttribute("list",list);
			return "user_planned_application.html";
		}
		
	}
	@GetMapping("/show/cart")
	public String getShowCartPage(Model model) {
		if(session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list  = new LinkedList<LessonEntity>();
			model.addAttribute("list",list);
		}else {
			LinkedList<LessonEntity> list  = (LinkedList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("list",list);
		}
		return "user_planned_application.html";
	}
	@GetMapping("/cart/delete/{lessonId}")
	public String getCartDelete(@PathVariable Long lessonId, Model model) {
		LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
        int idx = 0;
        Iterator<LessonEntity> ite = list.iterator();
        while(ite.hasNext()){
            LessonEntity entity = ite.next();
            if(entity.getLessonId().equals(lessonId)){
                break;
                
            }
            idx++;
        }
        list.remove(idx);
		
    	return "redirect:/lesson/show/cart";
	}
}
