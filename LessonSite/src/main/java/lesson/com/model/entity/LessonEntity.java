package lesson.com.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "lesson")
public class LessonEntity {

	public LessonEntity(@NonNull LocalDate startDate, @NonNull LocalTime startTime, @NonNull LocalTime finishTime,
			@NonNull String lessonName, String lessonDetail, int lessonFee, String imageName,
			LocalDateTime registerDate,Long adminId) {
		this.startDate = startDate;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.lessonName = lessonName;
		this.lessonDetail = lessonDetail;
		this.lessonFee = lessonFee;
		this.imageName = imageName;
		this.registerDate = registerDate;
		this.adminId = adminId;
	}
	
	


	@Id
	@Column(name = "lesson_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lessonId;

	@NonNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date")
	private LocalDate startDate;

	@NonNull
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "start_time")
	private LocalTime startTime;

	@NonNull
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "finish_time")
	private LocalTime finishTime;

	@NonNull
	@Column(name = "lesson_name")
	private String lessonName;

	@Column(name = "lesson_detail")
	private String lessonDetail;

	@Column(name = "lesson_fee")
	private int lessonFee;

	@Column(name = "image_name")
	private String imageName;
	
	@Column(name="admin_id")
	private Long adminId;

	@Column(name = "register_date")
	private LocalDateTime registerDate;
}
