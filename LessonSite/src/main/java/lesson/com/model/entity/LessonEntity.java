package lesson.com.model.entity;

import java.sql.Date;
import java.sql.Time;

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
@Table(name="lesson")
public class LessonEntity {
	@Id
	@Column(name="lesson_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lessonId;
	
	@NonNull
	@Column(name="start_date")
	private Date startDate;

	@NonNull
	@Column(name="start_time")
	private Time startTime;

	@NonNull
	@Column(name="lesson_name")
	private String lessonName;
	
	@Column(name="lesson_detail")
	private String lessonDetail;
	
	@Column(name="lesson_fee")
	private int lessonFee;
	
	@Column(name="image_id")
	private Long imageId;
	
	@Column(name="register_date")
	private String registerDate;
}
