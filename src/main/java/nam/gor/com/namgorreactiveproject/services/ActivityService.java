package nam.gor.com.namgorreactiveproject.services;

import nam.gor.com.namgorreactiveproject.dao.ActivityDao;
import nam.gor.com.namgorreactiveproject.dao.ProjectDao;
import nam.gor.com.namgorreactiveproject.exceptions.BusinessException;
import nam.gor.com.namgorreactiveproject.models.Activity;
import nam.gor.com.namgorreactiveproject.models.Execution;
import nam.gor.com.namgorreactiveproject.models.Grade;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class ActivityService {

	private final ActivityDao activityDao;

	private final ProjectDao projectDao;

	@Autowired
	public ActivityService(ActivityDao activityDao,
						   ProjectDao projectDao) {
		this.activityDao = activityDao;
		this.projectDao = projectDao;
	}

	public Mono<Activity> save(Activity act) {
		return projectDao
				.findById(act.getProjectId())
				.switchIfEmpty(Mono.error(new BusinessException(
								HttpStatus.BAD_REQUEST,
						"The project code entered is not registered")))
				.flatMap(p -> "IN THE PIPELINE"
						.equals(p.getStatus()) ?
						activityDao.save(act) : Mono.error(new BusinessException(
										HttpStatus.CONFLICT,
								        "The project is not in " +
										"IN THE PIPELINE status")));
	}

	public Flux<Activity> findAll() {
		return this.activityDao.findAll();
	}

	public Mono<Activity> publish(String id) {
		return activityDao
				.findById(id)
				.switchIfEmpty(Mono.error(new BusinessException(
						HttpStatus.BAD_REQUEST,
						"The id entered does not exist")))
				.flatMap(act -> activityDao
						      .existsByPublishDate(DateFormatUtils.format(new Date(),
											"yyyy-MM-dd"))
						.flatMap(p -> {
							if (!p) {
								act.setStatus("PUBLISHED");
								act.setOrderDate(DateFormatUtils
										               .format(new Date(),
												"yyyy-MM-dd"));
								return activityDao.save(act);
							} else {
								return Mono.error(new BusinessException(
												HttpStatus.CONFLICT,
										"You cannot publish a post on the same day"));
							}
						}));
	}

	public Mono<Activity> execution(String id,
									Execution execution) {
		return activityDao
				.findById(id)
				.switchIfEmpty(Mono.error(
						new BusinessException(HttpStatus.BAD_REQUEST,
							   	"The activity code entered is not registered")))
				.flatMap(act -> {
					if ("DRAFT".equals(act.getStatus())) {
						return Mono.error(new BusinessException(
								HttpStatus.CONFLICT,
					"You can only register execution on a published activity"));
					} else {
						execution.setExecuteDate(new Date());
						act.getExecutions().add(execution);
						return activityDao.save(act);
					}
				});
	}

	public Mono<Activity> grade(String id, Grade grade) {
		return activityDao
				.findById(id)
				.switchIfEmpty(Mono.error(
								new BusinessException(HttpStatus.BAD_REQUEST,
								"The activity code entered is not registered")))
				.flatMap(act -> {
					if (act
							.getGrades()
							.stream()
							.anyMatch(str -> str
									       .getUserId()
									       .equals(grade.getUserId()))) {
						return Mono.error(new BusinessException(HttpStatus.CONFLICT,
										"User already registered a grade"));
					}
					grade.setGradeDate(new Date());
					act.getGrades().add(grade);
					return activityDao.save(act);
				});
	}
}
