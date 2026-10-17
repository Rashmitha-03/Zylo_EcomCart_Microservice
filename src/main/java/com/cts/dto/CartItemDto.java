package com.cts.dto;

import lombok.Data;

@Data
public class CartItemDto {

	private int cartItemID;
	private int userdto;
	private int productID;
	private int quantity;
	private double totalPrice;

}
