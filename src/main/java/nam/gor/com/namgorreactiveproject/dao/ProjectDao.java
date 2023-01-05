package nam.gor.com.namgorreactiveproject.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import nam.gor.com.namgorreactiveproject.models.Project;
import reactor.core.publisher.Flux;

@Repository
public interface ProjectDao extends ReactiveMongoRepository<Project, String> {
	
	Flux<Project> findByCustomerId(String customerId);
}