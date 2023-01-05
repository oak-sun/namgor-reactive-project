package nam.gor.com.namgorreactiveproject.handlers;

import nam.gor.com.namgorreactiveproject.models.Activity;
import nam.gor.com.namgorreactiveproject.models.Execution;
import nam.gor.com.namgorreactiveproject.models.Grade;
import nam.gor.com.namgorreactiveproject.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ActivityHandler {

    @Autowired
    private ActivityService service;

    public Mono<ServerResponse> save(ServerRequest req) {
        return req
                .bodyToMono(Activity.class)
                .flatMap(act -> this.service.save(act))
                .flatMap(act -> ServerResponse
                                 .ok()
                                .body(Mono.just(act),
                                        Activity.class));
    }

    public Mono<ServerResponse> findAll(ServerRequest req) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(service.findAll(),
                        Activity.class);
    }
    
    public Mono<ServerResponse> publish(ServerRequest req) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(service.publish(req.pathVariable("id")),
                                                      Activity.class);
    }
    
    public Mono<ServerResponse> execution(ServerRequest req) {
    	return req
                .bodyToMono(Execution.class)
                .flatMap(ex -> this.service
                               .execution(req.pathVariable("id"), ex))
                .flatMap(a -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(a), Activity.class));
    }
    
    public Mono<ServerResponse> grade(ServerRequest req) {

    	return req
                .bodyToMono(Grade.class)
                .flatMap(g -> this.service
                               .grade(req.pathVariable("id"), g))
                .flatMap(a -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(a), Activity.class));
    }
}
