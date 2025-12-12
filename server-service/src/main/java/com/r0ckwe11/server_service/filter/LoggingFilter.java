package com.r0ckwe11.server_service.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        System.out.println("SERVER REQUEST: " +
                exchange.getRequest().getMethod() + " " +
                exchange.getRequest().getURI());

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    System.out.println("SERVER RESPONSE: " +
                            exchange.getResponse().getStatusCode());
                });
    }
}
