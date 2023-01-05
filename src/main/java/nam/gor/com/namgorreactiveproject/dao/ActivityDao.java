package nam.gor.com.namgorreactiveproject.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nam.gor.com.namgorreactiveproject.models.Activity;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface ActivityDao extends ReactiveMongoRepository<Activity, String> {
	
    @Query(value = "select case when count(p)> 0 then true else false end from Activity p where CAST(p.publishDate AS date) = CAST(:publishDate AS date)")
    Mono<Boolean> existsActivityPublished(@Param("publishDate")
										  Date publishDate);
	
     @Query(value = "{publishDate: ?0}",
			 count= true)
     Mono<Integer> getActivitiesCountByPublishDate(Date publishDate);
	
	Mono<Boolean> existsByPublishDate(String publishDate);
}
