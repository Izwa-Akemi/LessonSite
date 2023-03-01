package lesson.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lesson.com.model.entity.StudentEntity;
import lesson.com.model.entity.UuIdEntity;

public interface UuIdDao extends JpaRepository<UuIdEntity, Long> {
	UuIdEntity save(UuIdEntity uuIdEntity);
	UuIdEntity findByUuidMail(String uuidMail);
}
