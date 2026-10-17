package com.cts.dto;



import java.util.List;

import lombok.Data;

@Data
public class CartDto {
	private int cartId;
	private int userID;
    private double totalPrice;
    private List<CartItemDto> items;
}
