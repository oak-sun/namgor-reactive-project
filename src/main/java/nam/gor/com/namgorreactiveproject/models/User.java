package nam.gor.com.namgorreactiveproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="users")
public class User {

	private String userId;
	private String login;
	private String password;
	private String customerId;
	
}
