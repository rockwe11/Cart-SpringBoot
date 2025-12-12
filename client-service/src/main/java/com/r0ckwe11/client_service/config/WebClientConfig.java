package com.r0ckwe11.client_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081") // сервер
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            System.out.println("REQUEST: " + request.method() + " " + request.url());
            return next.exchange(request);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return (request, next) -> next.exchange(request)
                .doOnNext(response ->
                        System.out.println("RESPONSE: " + response.statusCode()));
    }
}
