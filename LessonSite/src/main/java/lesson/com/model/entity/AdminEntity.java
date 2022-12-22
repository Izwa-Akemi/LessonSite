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
@Table(name="admin")
public class AdminEntity {
	@Id
	@Column(name="admin_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long adminId;
	
	@NonNull
	@Column(name="admin_name")
	private String adminName;

	@NonNull
	@Column(name="admin_email")
	private String adminEmail;

	@NonNull
	@Column(name="admin_password")
	private String adminPassword;
	
	@Column(name="delete_flg")
	private int deleteFlg;
	
	@Column(name="register_date")
	private Timestamp registerDate;
}
