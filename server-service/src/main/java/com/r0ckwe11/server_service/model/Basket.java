package com.r0ckwe11.server_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {
    private List<Item> items;
    private double totalPrice;
    private boolean discountApplied;
}
