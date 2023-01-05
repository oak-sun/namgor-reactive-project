package nam.gor.com.namgorreactiveproject.dao;

import nam.gor.com.namgorreactiveproject.models.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerDao extends ReactiveMongoRepository<Customer, String> {

    Flux<Customer> findByEmail(String email);

    Flux<Customer> findByName(String name);

    Mono<Boolean>existsByEmail(String email);
}
