package com.r0ckwe11.server_service.service;

import com.r0ckwe11.server_service.dao.ItemRepository;
import com.r0ckwe11.server_service.model.Basket;
import com.r0ckwe11.server_service.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final ItemRepository itemRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public Mono<Basket> process(List<Integer> ids) {

        List<Item> collected = new ArrayList<>();

        // N+1: каждый item по одному
        for (Integer id : ids) {
            Item item = itemRepository.findByIdSlow(id);

            // Ненужная сериализация/десериализация
            try {
                String json = mapper.writeValueAsString(item);
                item = mapper.readValue(json, Item.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            collected.add(item);
        }

        // Лишние сортировки
        collected.sort(Comparator.comparing(Item::getName));
        collected.sort(Comparator.comparing(Item::getPrice).reversed());

        double total = 0;
        for (Item i1 : collected) {
            for (Item i2 : collected) {
                total += (i1.getPrice() + i2.getPrice()) * 0.0001;
            }
        }

        // Плохое преобразование: повторы
        boolean discountApplied = collected.size() > 2;

        Basket basket = new Basket(collected, total, discountApplied);

        return Mono.just(basket);
    }
}
