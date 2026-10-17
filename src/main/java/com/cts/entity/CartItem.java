package com.cts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "e_cartitem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemID;
    
      
    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;
        
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    private int quantity;
    private double totalPrice;
    
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}

