package nam.gor.com.namgorreactiveproject.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="customers")
public class Customer {
    @Id
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private Date partnershipDate;
}
