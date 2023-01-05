package nam.gor.com.namgorreactiveproject.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
public class GlobalExceptionHandler implements
		                            ErrorWebExceptionHandler {

	private final ObjectMapper mapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange,
							 Throwable ex) {

		var buffFact = exchange
				                            .getResponse()
				                            .bufferFactory();

		if (ex instanceof BusinessException internalExc) {
			exchange
					.getResponse()
					.setStatusCode(internalExc.getHttpStatus());
			DataBuffer dataBuffer;

			try {
				dataBuffer = buffFact.wrap(
						  mapper.writeValueAsBytes(new HttpError(
								  internalExc.getMessage())));
			} catch (JsonProcessingException e) {
				dataBuffer = buffFact.wrap("".getBytes());
			}
			exchange
					.getResponse()
					.getHeaders()
					.setContentType(MediaType.APPLICATION_JSON);
			return exchange
					.getResponse()
					.writeWith(Mono.just(dataBuffer));
		}

		exchange
				.getResponse()
				.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		exchange
				.getResponse()
				.getHeaders()
				.setContentType(MediaType.TEXT_PLAIN);
		var dataBuffer = buffFact
				                  .wrap("Unknown error".getBytes());
		return exchange
				.getResponse()
				.writeWith(Mono.just(dataBuffer));
	}

	@AllArgsConstructor
	@Getter
	public static class HttpError {
		String message;
	}
}
