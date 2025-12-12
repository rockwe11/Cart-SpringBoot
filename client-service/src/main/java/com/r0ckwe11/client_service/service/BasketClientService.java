package com.r0ckwe11.client_service.service;

import com.r0ckwe11.client_service.model.Basket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketClientService {

    private final WebClient webClient;

    public Mono<Basket> getBasket(List<Integer> ids) {

        return webClient.post()
                .uri("/basket")
                .bodyValue(ids)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Client error: " + response.statusCode()))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Server error: " + response.statusCode()))
                )
                .bodyToMono(Basket.class)
                .timeout(Duration.ofSeconds(2)) // таймаут
                        .retryWhen(
                                Retry.backoff(3, Duration.ofMillis(500))
                                        .maxBackoff(Duration.ofSeconds(5))
                        )
                .onErrorResume(e -> {
                    System.out.println("Handled error: " + e.getMessage());
                    // возвращаем fallback корзину
                    return Mono.just(new Basket(null, 0, false));
                });
    }
}
