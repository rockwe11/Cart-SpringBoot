package com.r0ckwe11.client_service.controller;

import com.r0ckwe11.client_service.model.Basket;
import com.r0ckwe11.client_service.service.BasketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final BasketClientService service;

    @PostMapping("/basket")
    public Mono<Basket> requestBasket(@RequestBody List<Integer> itemIds) {
        return service.getBasket(itemIds);
    }
}
