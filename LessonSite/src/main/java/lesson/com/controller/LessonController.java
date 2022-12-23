package lesson.com.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lesson.com.model.entity.AdminEntity;
import lesson.com.model.entity.LessonEntity;
import lesson.com.service.LessonService;

@Controller
@RequestMapping("/admin/lesson")
public class LessonController {
	@Autowired
	LessonService lessonService;
	@Autowired
	HttpSession session;
	
	@GetMapping("/register")
	public String getLessonRegisterPage() {
		return "admin_register_lesson.html";
	}
	@PostMapping("/register/create")
	public String registerLesson(@RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
			@RequestParam(name = "finishTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime finishTime,
			@RequestParam String lessonName,
			@RequestParam String lessonDetail,
			@RequestParam int lessonFee, 
			@RequestParam("imageName") MultipartFile imageName) {
		//画像ファイル名を取得する
		String fileName = imageName.getOriginalFilename();
		//ファイルのアップロード処理
		try {
			//画像ファイルの保存先を指定する。
			File lessonFile = new File("./src/main/resources/static/lesson-image/"+fileName);
			//画像ファイルからバイナリデータを取得する
			byte[] bytes = imageName.getBytes();
			//画像を保存（書き出し）するためのバッファを用意する
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(lessonFile));
			//画像ファイルの書き出しする。
			out.write(bytes);
			//バッファを閉じることにより、書き出しを正常終了させる。
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		lessonService.createLesson(startDate, startTime,finishTime, lessonName, lessonDetail, lessonFee,fileName);			
		return "admin_fix_register.html";
	}
	
	@GetMapping("/all")
	public String getLessonAllPage(Model model) {
		//現在ログインしている管理者情報を取得する
		AdminEntity admin = (AdminEntity) session.getAttribute("admin");
		//現在ログインしている人の名前を取得する
		String loginAdminName = admin.getAdminName();
		List<LessonEntity>lessonList = lessonService.findAllLesson();
		model.addAttribute("loginAdminName",loginAdminName);
		model.addAttribute("lessonList", lessonList);
		return "admin_lesson_lineup.html";
	}
	@GetMapping("/edit/{lessonId}")
	public String getLessonEditPage(@PathVariable Long lessonId,Model model) {
		//現在ログインしている管理者情報を取得する
		AdminEntity admin = (AdminEntity) session.getAttribute("admin");
		//現在ログインしている人の名前を取得する
		String loginAdminName = admin.getAdminName();
		LessonEntity lessonList  = lessonService.findByLessonId(lessonId);
		model.addAttribute("loginAdminName",loginAdminName);
		model.addAttribute("lessonList", lessonList);
		return "admin_edit_lesson.html";
	}

	@PostMapping("/edit/update")
	public String editLesson(@RequestParam Long lessonId,
			@RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
			@RequestParam(name = "finishTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime finishTime,
			@RequestParam String lessonName,
			@RequestParam String lessonDetail,
			@RequestParam int lessonFee,
			@RequestParam String imageName,
			Model model) {
		
		lessonService.updateLesson(lessonId, startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee,imageName);
		model.addAttribute("lessonId",lessonId);
		return "admin_fix_edit.html";
	}
	
	@GetMapping("/image/edit/{lessonId}")
	public String getLessonImageEditPage(@PathVariable Long lessonId,Model model) {
		//現在ログインしている管理者情報を取得する
		AdminEntity admin = (AdminEntity) session.getAttribute("admin");
		//現在ログインしている人の名前を取得する
		String loginAdminName = admin.getAdminName();
		LessonEntity lessonList  = lessonService.findByLessonId(lessonId);
		model.addAttribute("loginAdminName",loginAdminName);
		model.addAttribute("lessonList", lessonList);
		return "admin_edit_lesson_img.html";
	}
	@PostMapping("/image/edit/update")
	public String editLessonImage(@RequestParam Long lessonId,
			@RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
			@RequestParam(name = "finishTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime finishTime,
			@RequestParam String lessonName,
			@RequestParam String lessonDetail,
			@RequestParam int lessonFee,
			@RequestParam("imageName") MultipartFile imageName,Model model) {
		//画像ファイル名を取得する
		String fileName = imageName.getOriginalFilename();
		//ファイルのアップロード処理
		try {
			//画像ファイルの保存先を指定する。
			File lessonFile = new File("./src/main/resources/static/lesson-image/"+fileName);
			//画像ファイルからバイナリデータを取得する
			byte[] bytes = imageName.getBytes();
			//画像を保存（書き出し）するためのバッファを用意する
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(lessonFile));
			//画像ファイルの書き出しする。
			out.write(bytes);
			//バッファを閉じることにより、書き出しを正常終了させる。
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		lessonService.updateLesson(lessonId, startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee,fileName);
		model.addAttribute("lessonId",lessonId);
		return "admin_fix_edit.html";
	}
	@GetMapping("/delete")
	public String getLessonDeletePage(Model model) {
		//現在ログインしている管理者情報を取得する
		AdminEntity admin = (AdminEntity) session.getAttribute("admin");
		//現在ログインしている人の名前を取得する
		String loginAdminName = admin.getAdminName();
		List<LessonEntity>lessonList = lessonService.findAllLesson();
		model.addAttribute("loginAdminName",loginAdminName);
		model.addAttribute("lessonList", lessonList);
		return "admin_delete_lesson.html";
	}
	@PostMapping("/delete/remove")
	public String lessonDelete(Long lessonId,Model model) {
		//現在ログインしている管理者情報を取得する
		AdminEntity admin = (AdminEntity) session.getAttribute("admin");
		//現在ログインしている人の名前を取得する
		String loginAdminName = admin.getAdminName();
		lessonService.deleteLesson(lessonId);
		model.addAttribute("loginAdminName",loginAdminName);
		return "admin_fix_delete.html";
	}
}
