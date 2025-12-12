package com.r0ckwe11.server_service.controller;

import com.r0ckwe11.server_service.model.Basket;
import com.r0ckwe11.server_service.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BasketController {

    private final BasketService service;

    @PostMapping("/basket")
    public Mono<Basket> getBasket(@RequestBody List<Integer> itemIds) {
        return service.process(itemIds);
    }
}
