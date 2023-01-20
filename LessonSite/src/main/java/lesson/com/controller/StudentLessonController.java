package lesson.com.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

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

import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.LessonEntity;
import lesson.com.model.entity.StudentEntity;
import lesson.com.model.entity.TransactionHistoryEntity;
import lesson.com.service.LessonService;
import lesson.com.service.TransactoinHistoryService;
import lesson.com.service.TransactoinItemService;

@Controller
@RequestMapping("/lesson")
public class StudentLessonController {
	@Autowired
	LessonService lessonService;
	@Autowired
	HttpSession session;
	@Autowired
	TransactoinHistoryService transactoinHistoryService;
	@Autowired
	TransactoinItemService transactoinItemService;
	@Autowired
	MailSender mailSender;
	
	@GetMapping("/menu")
	public String getLessonMenuPage(Model model) {
		List<LessonEntity> lessonList = lessonService.findActiveAllLesson();
		model.addAttribute("lessonList", lessonList);
		return "user_menu.html";
	}

	@GetMapping("/detail/{lessonId}")
	public String getLessonDetailPage(@PathVariable Long lessonId, Model model) {
		LessonEntity lesson = lessonService.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		return "user_lesson_detail.html";
	}

	public boolean isLessonExist(Long lessonId, LinkedList<LessonEntity> list) {
		Iterator<LessonEntity> ite = list.iterator();
		boolean isExist = false;
		while (ite.hasNext()) {
			LessonEntity lesson = ite.next();
			if (lesson.getLessonId().equals(lessonId)) {
				isExist = true;
				break;
			}
		}
		return isExist;

	}

	@PostMapping("/cart/all")
	public String addCartPage(@RequestParam Long lessonId, Model model) {
		
		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			list.add(lessons);
			session.setAttribute("cart", list);
			if(session.getAttribute("student") == null) {
				return "redirect:/user/login";
			}
			model.addAttribute("list", list);
			return "user_planned_application.html";
		} else {

			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			if (isLessonExist(lessonId, list)) {

			} else {
				list.add(lessons);
			}
			model.addAttribute("list", list);
			return "user_planned_application.html";
		}

	}

	@GetMapping("/show/cart")
	public String getShowCartPage(Model model) {
		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			model.addAttribute("list", list);
		} else {
			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("list", list);
		}
		return "user_planned_application.html";
	}

	@GetMapping("/cart/delete/{lessonId}")
	public String getCartDelete(@PathVariable Long lessonId, Model model) {
		LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
		int idx = 0;
		Iterator<LessonEntity> ite = list.iterator();
		while (ite.hasNext()) {
			LessonEntity entity = ite.next();
			if (entity.getLessonId().equals(lessonId)) {
				break;

			}
			idx++;
		}
		list.remove(idx);

		return "redirect:/lesson/show/cart";
	}

	@GetMapping("/request")
	public String getRequestPage(Model model) {
		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			model.addAttribute("list", list);
		} else {
			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("list", list);
		}
		return "user_apply_select_payment.html";
	}

	@PostMapping("/confirm")
	public String getRequestConfirm(@RequestParam int payment, Model model) {
		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			model.addAttribute("list", list);
		} else {
			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("list", list);
		}
		String[] pays = { "当日現金支払い", "事前銀行振込", "クレジットカード決済" };
		model.addAttribute("payMethod", pays[payment]);
		return "user_confirm_apply_detail.html";
	}

	@GetMapping("/complete")
	public String getComplete(Model model) {
		LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
		//合計金額の計算
		int total = 0;
		Iterator<LessonEntity> ite = list.iterator();
		while (ite.hasNext()) {
			LessonEntity entity = ite.next();
			total += entity.getLessonFee();
		}
		StudentEntity student = (StudentEntity) session.getAttribute("student");
		//transactoin_historyに保存
		transactoinHistoryService.createTransactoinHistory(student.getStudentId(), total);
		//student_idを使用して最新のtransaction_idを取得
		TransactionHistoryEntity latestTransactions  = transactoinHistoryService.getTransactoinId(student.getStudentId());
		//transactoin_itemに保存
		Iterator<LessonEntity> ite2 = list.iterator();
		while (ite2.hasNext()) {
			LessonEntity entity = ite2.next();
			transactoinItemService.createTransactoinHistory(latestTransactions.getTransactionId(),entity.getLessonId());
		}
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
        msg.setTo(student.getStudentEmail()); // 送信先メールアドレス
        msg.setSubject("購入ありがとう"); // タイトル               
        msg.setText("テスト本文\r\n改行します。"); //本文

        try {
            mailSender.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }	
	
		list.clear();
		return "user_apply_complete.html";
	}
}
