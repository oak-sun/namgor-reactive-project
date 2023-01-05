package nam.gor.com.namgorreactiveproject.models;

import java.util.ArrayList;
import java.util.List;

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
@Document(value="activities")
public class Activity {

    @Id
    private String activityId;
    private String projectId;
    private String title;
    private String orderDate;
    private String status;
    private String content;
    private List<Execution> executions = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();
    
    
    public void addExecution(Execution execution) {
    	if (this.executions == null) {
			this.executions = new ArrayList<>();
		}
    	this.executions.add(execution);
    }

    public void addGrade(Grade grade) {
    	if (this.grades == null) {
			this.grades = new ArrayList<>();
		}
    	this.grades.add(grade);
    }
}
