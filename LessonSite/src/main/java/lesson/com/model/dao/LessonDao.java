package lesson.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import lesson.com.model.entity.LessonEntity;

@Repository
public interface LessonDao extends JpaRepository<LessonEntity, Long> {
	LessonEntity save(LessonEntity lesonEntity);
	LessonEntity findByLessonId(Long lessonId);
	List<LessonEntity>findAll();
	@Transactional
	List<LessonEntity> deleteByLessonId(Long blogId);
}
