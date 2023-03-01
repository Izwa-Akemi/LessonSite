package lesson.com.model.entity;

import java.io.Serializable;
import lombok.Data;


@Data
public class AdminSubKey implements Serializable {
	private Long adminId;
	private Long lessonId;
}
