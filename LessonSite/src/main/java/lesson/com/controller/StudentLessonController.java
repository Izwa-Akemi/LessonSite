package lesson.com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import jakarta.servlet.http.HttpSession;
import lesson.com.model.dao.SubscriptionDao;
import lesson.com.model.entity.LessonEntity;
import lesson.com.model.entity.StudentEntity;
import lesson.com.model.entity.SubscriptionEntity;
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

	@Autowired
	SubscriptionDao subscriptionDao;

	@GetMapping("/menu")
	public String getLessonMenuPage(Model model) {
		List<LessonEntity> lessonList = lessonService.findActiveAllLesson();
		model.addAttribute("lessonList", lessonList);
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
		return "user_menu.html";
	}
	@GetMapping("/menu/logout")
	public String getLessonLogoutMenuPage(Model model) {
		List<LessonEntity> lessonList = lessonService.findActiveAllLesson();
		model.addAttribute("lessonList", lessonList);
		model.addAttribute("loginFlg", false);
		model.addAttribute("userName", null);
		model.addAttribute("logoutMessage","ログアウトしました");
		return "user_menu.html";
	}
	

	@GetMapping("/detail/{lessonId}")
	public String getLessonDetailPage(@PathVariable Long lessonId, Model model) {
		LessonEntity lesson = lessonService.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
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

	public boolean loginCheck() {
		if (session.getAttribute("student") == null) {
			return false;
		} else {
			return true;
		}
	}

	public String loginUserName() {
		if (loginCheck() == true) {
			StudentEntity student = (StudentEntity) session.getAttribute("student");
			String studentName = student.getStudentName();
			return studentName;
		} else {
			return null;
		}
	}

	@GetMapping("/mypage")
	public String getMypage(Model model) {
		if (session.getAttribute("student") == null) {
			return "redirect:/user/login";
		}
		StudentEntity student = (StudentEntity) session.getAttribute("student");
		Long studentId = student.getStudentId();
		List<SubscriptionEntity> listSub = subscriptionDao.findByStudentId(studentId);
		model.addAttribute("listSub", listSub);
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", student.getStudentName());
		return "mypage.html";
	}

	@PostMapping("/cart/all")
	public String addCartPage(@RequestParam Long lessonId, Model model) {

		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			list.add(lessons);
			session.setAttribute("cart", list);
			if (session.getAttribute("student") == null) {
				return "redirect:/user/login";
			}
			model.addAttribute("list", list);
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", loginUserName());
			return "user_planned_application.html";
		} else {

			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			if (isLessonExist(lessonId, list)) {

			} else {
				list.add(lessons);
			}

			model.addAttribute("list", list);
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", loginUserName());
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
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
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
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", loginUserName());
		} else {
			LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("list", list);
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", loginUserName());
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
			// 合計金額の計算
			int total = 0;
			Iterator<LessonEntity> ite = list.iterator();
			while (ite.hasNext()) {
				LessonEntity entity = ite.next();
				total += entity.getLessonFee();
			}
			model.addAttribute("list", list);
			model.addAttribute("amount", total);
			session.setAttribute("total", total);
		}

		String[] pays = { "当日現金支払い", "事前銀行振込", "クレジットカード決済" };
		session.setAttribute("payment", pays[payment]);
		boolean payFlg = false;
		if (pays[payment].equals("クレジットカード決済")) {
			payFlg = true;
		}
		model.addAttribute("payFlg", payFlg);
		model.addAttribute("payMethod", pays[payment]);
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
		return "user_confirm_apply_detail.html";
	}

	@GetMapping("/complete")
	public String getComplete(Model model) {
		LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");
		// 合計金額の計算
		int total = 0;
		Iterator<LessonEntity> ite = list.iterator();
		while (ite.hasNext()) {
			LessonEntity entity = ite.next();
			total += entity.getLessonFee();
		}
		StudentEntity student = (StudentEntity) session.getAttribute("student");
		// transactoin_historyに保存
		transactoinHistoryService.createTransactoinHistory(student.getStudentId(), total);
		// student_idを使用して最新のtransaction_idを取得
		TransactionHistoryEntity latestTransactions = transactoinHistoryService
				.getTransactoinId(student.getStudentId());
		// transactoin_itemに保存
		Iterator<LessonEntity> ite2 = list.iterator();
		while (ite2.hasNext()) {
			LessonEntity entity = ite2.next();
			transactoinItemService.createTransactoinHistory(latestTransactions.getTransactionId(),
					entity.getLessonId());
		}
		LinkedList<LessonEntity> list2 = (LinkedList<LessonEntity>) session.getAttribute("cart");
		Iterator<LessonEntity> ite3 = list2.iterator();
		List<Object> lineItems = new ArrayList<>();
		while (ite3.hasNext()) {
			LessonEntity entity = ite3.next();
			Map<String, Object> params = new HashMap<>();
			params.put("price", entity.getLessonFee());
			params.put("quantity", 1);
			params.put("name", entity.getLessonName());
			params.put("description", entity.getLessonDetail());
			lineItems.add(params);
		}
		session.setAttribute("total", total);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
		msg.setTo(student.getStudentEmail()); // 送信先メールアドレス
		msg.setSubject("購入ありがとう"); // タイトル
		msg.setText("テスト本文\r\n改行します。"); // 本文

		try {
			mailSender.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
		}

		list.clear();
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
		return "user_apply_complete.html";
		// }

	}

	@PostMapping("/pay")
	public String getPayment(@RequestParam("stripeToken") String stripeToken,
			@RequestParam("stripeTokenType") String stripeTokenType, @RequestParam("stripeEmail") String stripeEmaill) {
		Stripe.apiKey = "sk_test_51K0KmoGyEksY9WnlVbMRq7SxWsXPtanyBfnL8X7qty6p0WUgyNdGCYSw8ZeHL3oY9WKsWcEAaq4Hl0aUrzd7cE0x00hPCBQ4kl";

		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", session.getAttribute("total"));
		chargeMap.put("currency", "jpy");
		chargeMap.put("source", stripeToken);

		try {
			Charge charge = Charge.create(chargeMap);
			System.out.println(charge);
		} catch (StripeException e) {
			e.printStackTrace();
		}
		LinkedList<LessonEntity> list = (LinkedList<LessonEntity>) session.getAttribute("cart");

		StudentEntity student = (StudentEntity) session.getAttribute("student");
		// transactoin_historyに保存
		transactoinHistoryService.createTransactoinHistory(student.getStudentId(), (int) session.getAttribute("total"));
		// student_idを使用して最新のtransaction_idを取得
		TransactionHistoryEntity latestTransactions = transactoinHistoryService
				.getTransactoinId(student.getStudentId());
		// transactoin_itemに保存
		Iterator<LessonEntity> ite2 = list.iterator();
		while (ite2.hasNext()) {
			LessonEntity entity = ite2.next();
			transactoinItemService.createTransactoinHistory(latestTransactions.getTransactionId(),
					entity.getLessonId());
		}
		ResponseEntity.ok().build();
		session.removeAttribute("cart");
		session.removeAttribute("payment");
		session.removeAttribute("total");
		return "user_apply_complete.html";

	}
	@PostMapping("/history/delete")
	public String delete(@RequestParam Long transactionId) {
		transactoinItemService.deleteTransactoinId(transactionId);
		transactoinHistoryService.deleteTransactoinId(transactionId);
		return "redirect:/lesson/mypage";
	}
	
	
}
