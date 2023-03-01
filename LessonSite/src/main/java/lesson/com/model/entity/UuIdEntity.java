package lesson.com.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@Table(name = "uuid")
public class UuIdEntity {
	
	
	
	public UuIdEntity(@NonNull String mail, @NonNull String uuidMail, LocalDateTime exp) {
		this.mail = mail;
		this.uuidMail = uuidMail;
		this.exp = exp;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@Column(name = "mail")
	private String mail;
	
	@NonNull
	@Column(name = "uuid_mail")
	private String uuidMail;
	
	@Column(name = "exp")
	private LocalDateTime exp;

	
}
