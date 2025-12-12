package com.r0ckwe11.server_service.dao;

import com.r0ckwe11.server_service.model.Item;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ItemRepository {

    // "База данных"
    private static final List<Item> items = Arrays.asList(
            new Item(1, "Keyboard", 120.0),
            new Item(2, "Mouse", 50.0),
            new Item(3, "Monitor", 400.0),
            new Item(4, "USB Cable", 10.0),
            new Item(5, "Laptop", 1500.0)
    );

    // Медленный метод поиска (искусственная задержка)
    public Item findByIdSlow(Integer id) {
        try {
            Thread.sleep(50); // имитация задержки БД
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
