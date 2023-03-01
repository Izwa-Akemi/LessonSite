package lesson.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lesson.com.model.entity.AdminAndLessonEntity;

public interface AdminAndLessonDao extends JpaRepository<AdminAndLessonEntity, Long> {
@Query(value="select a.admin_id,a.admin_name,l.start_date,l.start_time,l.finish_time,l.lesson_name,l.lesson_detail,l.lesson_fee,l.image_name,l.lesson_id from admin a inner join lesson l on a.admin_id = l.admin_id where l.admin_id =?1",nativeQuery = true)
List<AdminAndLessonEntity>findLessonAll(Long adminId);
}
