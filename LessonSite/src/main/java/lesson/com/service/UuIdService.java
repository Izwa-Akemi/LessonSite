package lesson.com.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.UuIdDao;
import lesson.com.model.entity.LessonEntity;
import lesson.com.model.entity.StudentEntity;
import lesson.com.model.entity.UuIdEntity;

@Service
public class UuIdService {
	@Autowired
	UuIdDao uuIdDao;
	//保存処理
			public String createUUID(String userEmail) {
				UUID uuid =UUID.randomUUID();
				String uuidMail = uuid.toString();
				LocalDateTime dateTimeNow = LocalDateTime.now();
				LocalDateTime exp = dateTimeNow.plusHours(1);
				uuIdDao.save(new UuIdEntity(userEmail,uuidMail,exp));
				return uuidMail;
			}
			
			public UuIdEntity getByUuId(String uuIdMail) {
				return uuIdDao.findByUuidMail(uuIdMail);
			}
}
