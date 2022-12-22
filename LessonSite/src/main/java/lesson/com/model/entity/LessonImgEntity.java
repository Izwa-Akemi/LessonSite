package lesson.com.model.entity;

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
@Table(name="lesson_img")
public class LessonImgEntity {
	@Id
	@Column(name="image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long adminId;
	
	@NonNull
	@Column(name="image_name")
	private String adminName;
}
