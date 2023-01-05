package nam.gor.com.namgorreactiveproject.handlers;

import nam.gor.com.namgorreactiveproject.models.Customer;
import nam.gor.com.namgorreactiveproject.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class CustomerHandler {

	@Autowired
	private CustomerService service;

	public Mono<ServerResponse> findAll(ServerRequest req) {
		return ServerResponse
				.ok()
				.contentType(APPLICATION_JSON)
				.body(service.findAll(), Customer.class);
	}

	public Mono<ServerResponse> findById(ServerRequest req) {
		return this
				.service
				.findById(req.pathVariable("id"))
				.flatMap(c -> ServerResponse
						.ok()
						.body(Mono.just(c), Customer.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> findByEmail(ServerRequest req) {
		return ServerResponse
				.ok()
				.contentType(APPLICATION_JSON)
				.body(service
						.findByEmail(req
								.queryParam("email").get()),
						Customer.class);
	}

	public Mono<ServerResponse> save(ServerRequest req) {
		return req
				.bodyToMono(Customer.class)
				.flatMap(customer -> this.service.save(customer))
				.flatMap(a -> ServerResponse
						.ok()
						.contentType(APPLICATION_JSON)
						.body(Mono.just(a), Customer.class));
	}

	public Mono<ServerResponse> delete(ServerRequest req) {
		return this
				.service
				.delete(req.pathVariable("id"))
				.then(ServerResponse.ok().build());

	}
}
