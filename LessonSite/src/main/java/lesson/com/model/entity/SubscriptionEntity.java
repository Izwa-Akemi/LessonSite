package lesson.com.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@IdClass(value=KeyEntity.class)
public class SubscriptionEntity {
	@Id
	@Column(name = "lesson_id")
	private Long lessonId;
	
	@Id
	@Column(name = "transaction_id")
	private Long transactionId;
	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "start_time")
	private LocalTime startTime;

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
	
	@Column(name="student_id")
	private Long studentId;
	
	@Column(name="amount")
	private int amount;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name="transaction_date")
	private LocalDateTime transactionDate;
}
