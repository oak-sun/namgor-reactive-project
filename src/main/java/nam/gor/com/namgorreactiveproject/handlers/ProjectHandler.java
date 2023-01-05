package nam.gor.com.namgorreactiveproject.handlers;

import nam.gor.com.namgorreactiveproject.models.Project;
import nam.gor.com.namgorreactiveproject.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ProjectHandler {
    @Autowired
    private ProjectService service;

    public Mono<ServerResponse> findAll(ServerRequest req) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(service.findAll(), Project.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        return this
                .service
                .findById(req.pathVariable("id"))
                .flatMap(project ->
                        ServerResponse.ok().body(
                                Mono.just(project), Project.class))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> save(ServerRequest req) {
        return req
                .bodyToMono(Project.class)
                .flatMap(project -> this.service.save(project))
                .flatMap(project -> ServerResponse.ok().body(
                        Mono.just(project), Project.class));
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        if (!isValidId(
                req.pathVariable("id"))) return badRequest().build();
        return this
                .service
                .delete(req.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

    public boolean isValidId(String id) {
        return id != null && id.length() > 5;
    }

}
