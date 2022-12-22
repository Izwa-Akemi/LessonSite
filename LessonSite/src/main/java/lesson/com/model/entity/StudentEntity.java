package lesson.com.model.entity;

import java.sql.Timestamp;

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
@Table(name="student")
public class StudentEntity {
	@Id
	@Column(name="student_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long studentId;
	
	@NonNull
	@Column(name="student_name")
	private String studentName;

	@NonNull
	@Column(name="student_email")
	private String studentEmail;

	@NonNull
	@Column(name="student_password")
	private String studentPassword;
	
	@Column(name="delete_flg")
	private int deleteFlg;
	
	@Column(name="register_date")
	private Timestamp registerDate;
}
