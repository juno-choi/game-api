
package com.juno.gameapi.webclient_config;

import com.juno.gameapi.exception.api.ClientError;
import com.juno.gameapi.exception.api.ServerError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {
    private final Environment env;

    @Bean
    public WebClient webClient(){
        ExchangeFilterFunction errorFilter = ExchangeFilterFunction
                .ofResponseProcessor( clientResponse -> exchangeFilterResponseProcessor(clientResponse));

        return WebClient.builder().baseUrl(env.getProperty("client.url"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(errorFilter)
                .build();
    }

    private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatus status = response.statusCode();
        if (status.is5xxServerError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ServerError(status, body)));
        }
        if (status.is4xxClientError()) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> {
                        log.error("body = {}",body);
                        return Mono.error(new ClientError(status, body));
                    });
        }
        return Mono.just(response);
    }
}
