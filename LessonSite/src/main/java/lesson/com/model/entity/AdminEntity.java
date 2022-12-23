package lesson.com.model.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

	public AdminEntity(@NonNull String adminName, @NonNull String adminEmail, @NonNull String adminPassword,
			LocalDateTime registerDate) {
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
		this.registerDate = registerDate;
	}
	
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
	private LocalDateTime registerDate;


}
