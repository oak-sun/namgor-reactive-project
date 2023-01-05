package nam.gor.com.namgorreactiveproject.services;

import nam.gor.com.namgorreactiveproject.dao.CustomerDao;
import nam.gor.com.namgorreactiveproject.exceptions.BusinessException;
import nam.gor.com.namgorreactiveproject.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService  {

	private final CustomerDao dao;

	@Autowired
	public CustomerService(CustomerDao dao) {
		this.dao = dao;
	}

	public Mono<Customer> findById(String id) {
		return this.dao.findById(id);
	}

	public Mono<Boolean> existsByEmail(String email) {
		return dao.existsByEmail(email);
	}
	public Flux<Customer> findByEmail(String email) {
		return this.dao.findByEmail(email);
	}

	public Flux<Customer> findByName(String name) {
		return this.dao.findByName(name);
	}

	public Flux<Customer> findAll() {
		return this.dao.findAll();
	}

	public Mono<Customer> save(Customer customer) {
		return this.dao
				.existsByEmail(customer.getEmail())
				.flatMap(saved -> !saved ?
						this.dao.save(customer) :
						Mono.error(new BusinessException(HttpStatus.BAD_REQUEST,
								"Customer saved")));
	}

	public Mono<Void> delete(String id) {
		return this
				.dao
				.findById(id)
				.switchIfEmpty(Mono.error(
						new BusinessException(HttpStatus.NOT_FOUND,
								"Customer deleted")))
				.flatMap(this.dao::delete);
	}
}
