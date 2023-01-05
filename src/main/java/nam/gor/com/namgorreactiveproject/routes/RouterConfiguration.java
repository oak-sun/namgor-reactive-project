package nam.gor.com.namgorreactiveproject.routes;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import nam.gor.com.namgorreactiveproject.handlers.CustomerHandler;
import nam.gor.com.namgorreactiveproject.handlers.ProjectHandler;
import nam.gor.com.namgorreactiveproject.handlers.ActivityHandler;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> projectRoutes(ProjectHandler handler) {
        return RouterFunctions
                .nest(RequestPredicates.path("/projects"),
                         RouterFunctions
                        .route(GET(""), handler::findAll)
                        .andRoute(GET("/{id}"), handler::findById)
                        .andRoute(POST("").and(contentType(APPLICATION_JSON)),
                                                  handler::save)
                        .andRoute(DELETE("/{id}"), handler::delete)
            );
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler handler){
        return RouterFunctions
                .nest(RequestPredicates.path("/customers"),
                       RouterFunctions
                .route(GET(""), handler::findAll)
                .andRoute(GET("/email"), handler::findByEmail)
                .andRoute(GET("/{id}"), handler::findById)
                .andRoute(POST("").and(accept(APPLICATION_JSON)),
                                   handler::save)
                .andRoute(DELETE("/{id}"), handler::delete)
            );
    }

    @Bean
    public RouterFunction<ServerResponse> activityRoutes(ActivityHandler handler){
        return RouterFunctions
                .nest(RequestPredicates.path("/activities"),
                        RouterFunctions.route(GET(""),
                                                handler::findAll)
                .andRoute(POST("").and(accept(APPLICATION_JSON)),
                          handler::save)
                .andRoute(PATCH("{id}/publish"), handler::publish)
                .andRoute(POST("{id}/execution").and(accept(APPLICATION_JSON)),
                          handler::execution)
                .andRoute(POST("{id}/grade").and(accept(APPLICATION_JSON)),
                          handler::grade)
                );
    }
}
