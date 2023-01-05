package nam.gor.com.namgorreactiveproject.services;


import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import nam.gor.com.namgorreactiveproject.exceptions.BusinessException;
import nam.gor.com.namgorreactiveproject.dao.CustomerDao;
import nam.gor.com.namgorreactiveproject.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import nam.gor.com.namgorreactiveproject.models.Project;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProjectService {

    private final ProjectDao projectDao;

    private final CustomerDao customerDao;

    @Autowired
    public ProjectService(ProjectDao projectDao, CustomerDao customerDao) {
        this.projectDao = projectDao;
        this.customerDao = customerDao;
    }

    public Mono<Project> findById(String id) {
        return  this.projectDao.findById(id);
    }

    public Flux<Project> findAll() {
        return projectDao.findAll();
    }

    public Mono<Project> save(Project project) {
    	return customerDao
                .findById(project.getCustomerId())
    			.switchIfEmpty(Mono.error(new BusinessException(
                                HttpStatus.BAD_REQUEST,
						"The customer code entered is not registered")))

                .flatMap(c -> Period
                                .between(Instant
                                    .ofEpochMilli(c.getPartnershipDate().getTime())
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(), LocalDate.now())
                               .getYears() < 3 ?
                                     Mono.empty() :
                                     Mono.just(c))
    	        .switchIfEmpty(Mono.error(
                              new BusinessException(HttpStatus.CONFLICT,
                              "The customer is not a long-time partner")))

                .flatMap(c -> projectDao
                .findByCustomerId(project.getCustomerId())
                .count()
                .flatMap(p -> p < 3 ?
                              projectDao.save(project) :
                              Mono.error(new BusinessException(HttpStatus.CONFLICT,
                              "The customer already has 3 registered projects"))));
    	
    }
    public Mono<Void> delete(String id) {
        return this
                .projectDao
                .findById(id)
                .doOnNext(p -> System.out.println("doOnNext p = " + p))
                .flatMap(this.projectDao::delete);
    }
}
