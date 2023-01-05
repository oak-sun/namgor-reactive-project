package nam.gor.com.namgorreactiveproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="grades")
public class Grade {
	
	private String type;
	private Date gradeDate;
	private String userId;
}
