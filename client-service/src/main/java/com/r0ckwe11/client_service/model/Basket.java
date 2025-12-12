package com.r0ckwe11.client_service.model;

import lombok.Data;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {
    private List<Item> items;
    private double totalPrice;
    private boolean discountApplied;
}
