package lesson.com.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.AdminAndLessonDao;
import lesson.com.model.dao.LessonDao;
import lesson.com.model.entity.AdminAndLessonEntity;
import lesson.com.model.entity.LessonEntity;

@Service
public class LessonService {
	@Autowired
	private LessonDao lessonDao;
	
	@Autowired
	private AdminAndLessonDao adminAndlessonDao;

	// 保存処理
	public void createLesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime, String lessonName,
			String lessonDetail, int lessonFee, String imageName,Long adminId) {
		LocalDateTime dateTimeNow = LocalDateTime.now();
		lessonDao.save(new LessonEntity(startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee,
				imageName,dateTimeNow,adminId));
	}

	// 一覧を取得
	public List<AdminAndLessonEntity> findAllLesson(Long adminId) {
		return adminAndlessonDao.findLessonAll(adminId);
	}

	// 生徒側一覧表示
	public List<LessonEntity> findActiveAllLesson() {
		LocalTime now = LocalTime.now();
		LocalDateTime dateTimeNow = LocalDateTime.now();
		List<LessonEntity> list = lessonDao.findAll();
		List<LessonEntity> retList = new LinkedList<LessonEntity>();
		Iterator<LessonEntity> ite = list.iterator();
		while (ite.hasNext()) {
			LessonEntity lesson = ite.next();
			LocalDateTime localDateTime = LocalDateTime.of(lesson.getStartDate(), lesson.getStartTime());
			if (dateTimeNow.compareTo(localDateTime) > 0) {

			} else {
				retList.add(lesson);
			}
		}
		return retList;
	}
	//カートに入れる
	

	public LessonEntity findByLessonId(Long lessonId) {
		return lessonDao.findByLessonId(lessonId);
	}

	public void updateLesson(Long lessonId, LocalDate startDate, LocalTime startTime, LocalTime finishTime,
			String lessonName, String lessonDetail, int lessonFee, String imageName,Long adminId) {
		LocalDateTime dateTimeNow = LocalDateTime.now();
		lessonDao.save(new LessonEntity(lessonId, startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee,
				imageName,adminId,dateTimeNow));
	}

// 削除
	public List<LessonEntity> deleteLesson(Long lessonId) {
		return lessonDao.deleteByLessonId(lessonId);
	}

}
