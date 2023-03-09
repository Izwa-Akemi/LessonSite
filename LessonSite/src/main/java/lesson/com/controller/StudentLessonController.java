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
import lesson.com.service.SubscriptionService;
import lesson.com.service.TransactoinHistoryService;
import lesson.com.service.TransactoinItemService;

@Controller
@RequestMapping("/lesson")
public class StudentLessonController {
	/**
	 * 講座テーブルにアクセスして操作するため、LessonServiceクラス を使えるようにしておきます。
	 */
	@Autowired
	LessonService lessonService;
	/**
	 * lesson,student,支払い情報をセッションから取得するために、 HttpSessionを取得可能にしておきます。
	 */
	@Autowired
	HttpSession session;
	/**
	 * 購入履歴テーブルにアクセスして操作するため、TransactoinHistoryServiceクラス を使えるようにしておきます。
	 */
	@Autowired
	TransactoinHistoryService transactoinHistoryService;
	/**
	 * 購入講座情報テーブルにアクセスして操作するため、TransactoinItemServiceクラス を使えるようにしておきます。
	 */
	@Autowired
	TransactoinItemService transactoinItemService;
	/**
	 * メール送信用クラス
	 */
	@Autowired
	MailSender mailSender;
	/**
	 * ユーザーが購入した商品一覧テーブルにアクセスして操作するため、SubscriptionServiceクラスを使えるようにしておきます。
	 */
	@Autowired
	SubscriptionService subscriptionService;
	/**
	 * メニュー画面の表示処理
	 */
	@GetMapping("/menu")
	public String getLessonMenuPage(Model model) {
		/**
		 * lessonテーブルから全ての講座情報を取得する
		 */
		List<LessonEntity> lessonList = lessonService.findActiveAllLesson();
		/**
		 * modelへ、lessonテーブルから取得した情報をセットする。
		 */
		model.addAttribute("lessonList", lessonList);
		/**
		 * modelへ、loginCheckメソッドから取得した情報をセットする。
		 */
		model.addAttribute("loginFlg", loginCheck());
		/**
		 * modelへ、loginUserNameメソッドから取得した情報をセットする。
		 */
		model.addAttribute("userName", loginUserName());
		return "user_menu.html";
	}

	/**
	 * ログアウトの処理
	 */
	@GetMapping("/menu/logout")
	public String getLessonLogoutMenuPage(Model model) {
		/**
		 * lessonテーブルから全ての講座情報を取得する
		 */
		List<LessonEntity> lessonList = lessonService.findActiveAllLesson();
		/**
		 * modelへ、lessonテーブルから取得した情報をセットする。
		 */
		model.addAttribute("lessonList", lessonList);
		/**
		 * modelへ、ログアウト処理のため、ログインフラグにfalseをセットする。
		 */
		model.addAttribute("loginFlg", false);
		/**
		 * modelへ、ログアウト処理のため ログインしているユーザー情報にnullをセットする。
		 */
		model.addAttribute("userName", null);
		/**
		 * modelへ、ログアウト処理が正常に完了したことを知らせるメッセージをセットする。
		 */
		model.addAttribute("logoutMessage", "ログアウトしました");
		return "user_menu.html";
	}
	/**
	 * 商品詳細の表示処理
	 */
	/**
	 * @param lessonId　講座Id
	 * @param model
	 * @return user_lesson_detail.html　講座情報詳細画面
	 */
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
	
	//ログインページに飛ばすメソッド
	public String login(String templete) {
		session.setAttribute("goLogin", templete);
		return "redirect:/user/login";
	}

	@GetMapping("/mypage")
	public String getMypage(Model model) {
		if (session.getAttribute("student") == null) {
			return login("/lesson/mypage");
		}else {
			StudentEntity student = (StudentEntity) session.getAttribute("student");
			Long studentId = student.getStudentId();
			List<SubscriptionEntity> listSub = subscriptionService.getPurchaseHistory(studentId);
			model.addAttribute("listSub", listSub);
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", student.getStudentName());
			return "mypage.html";
		}
				
	}

	@PostMapping("/cart/all")
	public String addCartPage(@RequestParam Long lessonId, Model model) {

		if (session.getAttribute("cart") == null) {
			LinkedList<LessonEntity> list = new LinkedList<LessonEntity>();
			LessonEntity lessons = lessonService.findByLessonId(lessonId);
			list.add(lessons);
			session.setAttribute("cart", list);
			if (session.getAttribute("student") == null) {
				return login("/lesson/show/cart");
			}else {
			model.addAttribute("list", list);
			model.addAttribute("loginFlg", loginCheck());
			model.addAttribute("userName", loginUserName());
			return "user_planned_application.html";
			}
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
		//List<Object> lineItems = new ArrayList<>();
		StringBuilder message = new StringBuilder();
		while (ite3.hasNext()) {
			LessonEntity entity = ite3.next();
//			Map<String, Object> params = new HashMap<>();
//			params.put("price", entity.getLessonFee());
//			params.put("quantity", 1);
//			params.put("name", entity.getLessonName());
//			params.put("description", entity.getLessonDetail());
//			lineItems.add(params);
			message.append("商品名:" + entity.getLessonName() + "" + "値段:" + entity.getLessonFee() + "円\n");

		}
		session.setAttribute("total", total);
		session.getAttribute("payment");
		SimpleMailMessage msg = new SimpleMailMessage();
		if (session.getAttribute("payment").equals("当日現金支払い")) {
			msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
			msg.setTo(student.getStudentEmail()); // 送信先メールアドレス
			msg.setSubject("ご購入ありがとうございます。"); // タイトル
			msg.setText("購入商品詳細となります。\r\n" + "ご確認何卒宜しくお願い致します。\r\n" + "支払い方法:当日現金払い\n" + message.toString() + "\n"
					+ "合計金額" + total + "円"); // 本文
		}
		if (session.getAttribute("payment").equals("事前銀行振込")) {
			msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
			msg.setTo(student.getStudentEmail()); // 送信先メールアドレス
			msg.setSubject("ご購入ありがとうございます。"); // タイトル
			msg.setText("購入商品詳細となります。\r\n" + "ご確認何卒宜しくお願い致します。\r\n" + "支払い方法:事前銀行振込\n" + message.toString() + "\n"
					+ "合計金額" + total + "円\n" + "下記口座に1週間以内に振り込みをお願い申し上げます。\n" + "口座情報：テスト銀行\n" + "支店名：東京支店\n"
					+ "口座番号:1234567812"); // 本文
		}

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
			@RequestParam("stripeTokenType") String stripeTokenType, @RequestParam("stripeEmail") String stripeEmaill,Model model) {
		Stripe.apiKey = "";

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

		StringBuilder message = new StringBuilder();
		while (ite2.hasNext()) {
			LessonEntity entity = ite2.next();
			transactoinItemService.createTransactoinHistory(latestTransactions.getTransactionId(),
					entity.getLessonId());
			message.append("商品名:" + entity.getLessonName() + "" + "値段:" + entity.getLessonFee() + "円\n");
		}

		ResponseEntity.ok().build();
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setFrom("a.izawa@rootcox.sakura.ne.jp"); // 送信元メールアドレス
		msg.setTo(student.getStudentEmail()); // 送信先メールアドレス
		msg.setSubject("ご購入ありがとうございます。"); // タイトル
		msg.setText("購入商品詳細となります。\r\n" + "ご確認何卒宜しくお願い致します。\r\n" + "支払い方法:クレジットカード決済\n" + message.toString() + "\n"
				+ "合計金額" + (int) session.getAttribute("total") + "円"); // 本文
		try {
			mailSender.send(msg);
		} catch (MailException e) {
			e.printStackTrace();
		}
		session.removeAttribute("cart");
		session.removeAttribute("payment");
		session.removeAttribute("total");
		model.addAttribute("loginFlg", loginCheck());
		model.addAttribute("userName", loginUserName());
		return "user_apply_complete.html";

	}

	@PostMapping("/history/delete")
	public String delete(@RequestParam Long transactionId) {
		transactoinItemService.deleteTransactoinId(transactionId);
		transactoinHistoryService.deleteTransactoinId(transactionId);
		return "redirect:/lesson/mypage";
	}

}
