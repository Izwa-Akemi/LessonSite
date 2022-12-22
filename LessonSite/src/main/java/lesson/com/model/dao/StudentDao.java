package lesson.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lesson.com.model.entity.StudentEntity;

public interface StudentDao extends JpaRepository<StudentEntity, Long> {
	StudentEntity save(StudentEntity studentEntity);
	StudentEntity findByStudentEmail(String studentEmail);
	List<StudentEntity> findByStudentEmailAndStudentPassword(String studentEmail, String studentPassword);
}
