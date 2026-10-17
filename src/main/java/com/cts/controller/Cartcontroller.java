package com.cts.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.CartDto;
import com.cts.service.Icartservice;

@RestController
@RequestMapping("/api/v1")
public class Cartcontroller {

    @Autowired
    private Icartservice cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddCartItemRequest request) {
        CartDto updatedCart = cartService.addItemToCart(request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

  
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable int userId, @PathVariable int productId) {
        CartDTO updatedCart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    
    
    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateCartItem(@RequestBody AddCartItemRequest request) {
        CartDTO updatedCart = cartService.updateCartItem(request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    // ✅ Get cart by user ID
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable int userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }
}
