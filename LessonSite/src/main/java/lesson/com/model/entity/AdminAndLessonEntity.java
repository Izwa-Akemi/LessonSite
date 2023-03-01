package lesson.com.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(value=AdminSubKey.class)
public class AdminAndLessonEntity {
	@Id
	@Column(name="admin_id")
	private Long adminId;
	
	@Id
	@Column(name="lesson_id")
	private Long lessonId;
	
	@Column(name="admin_name")
	private String adminName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "start_time")
	private LocalTime startTime;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "finish_time")
	private LocalTime finishTime;

	@Column(name = "lesson_name")
	private String lessonName;

	@Column(name = "lesson_detail")
	private String lessonDetail;

	@Column(name = "lesson_fee")
	private int lessonFee;

	@Column(name = "image_name")
	private String imageName;
	
	
}
