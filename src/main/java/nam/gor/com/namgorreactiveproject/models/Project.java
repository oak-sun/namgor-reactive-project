package nam.gor.com.namgorreactiveproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "projects")
public class Project {

    @Id
    private String projectId;

    @Field(name = "name")
    private String name;

    private String description;

    @Field(name = "customer")
    private String customerId;

    private String url;

    @Field(name = "status")
    private String status;
}
