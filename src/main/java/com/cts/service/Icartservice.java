package com.cts.service;

import com.cts.dto.CartDto;
import com.cts.dto.CartItemDto;

public interface Icartservice {
	
	CartDto addItemToCart(int userID, CartItemDto cartItemDto);

    CartDto removeItemFromCart(int userID, int cartItemItemID);

    CartDto updateItemQuantity(int userID, int cartItemID, int quantity);

    CartDto getCartByUserId(int userID);

}


