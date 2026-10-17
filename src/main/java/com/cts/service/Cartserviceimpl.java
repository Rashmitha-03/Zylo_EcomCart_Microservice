package com.cts.service;

import com.cts.dto.CartDto;
import com.cts.dto.CartItemDto;
import com.cts.entity.Cart;
import com.cts.entity.CartItem;
import com.cts.entity.Product;
import com.cts.entity.User;
import com.cts.exception.CartItemNotFoundException;
import com.cts.exception.CartNotFoundException;
import com.cts.exception.UserNotFoundException;
import com.cts.repository.CartItemRepository;
import com.cts.repository.ProductRepository;
import com.cts.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class Cartserviceimpl implements Icartservice {

	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  ProductRepository productRepository;
	@Autowired
    private  CartItemRepository cartRepository;
	@Autowired
    private  CartItemRepository cartItemRepository;
	@Autowired
    private  ModelMapper modelMapper;

   

    @Override
    public CartDto addItemToCart(int userID, CartItemDto cartItemDto) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userID));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        Product product = productRepository.findById(cartItemDto.getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemDto.getProductId()));

        // Check if item already exists in cart
        Optional<CartItem> existingCartItemOpt = cart.getItems() == null ? Optional.empty() :
                cart.getItems().stream()
                        .filter(item -> item.getProduct().getProductID() == product.getProductID())
                        .findFirst();

        CartItem cartItem;
        if (existingCartItemOpt.isPresent()) {
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
            BigDecimal totalPrice = BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            cartItem.setTotalPrice(totalPrice.doubleValue());
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setCart(cart);
            BigDecimal totalPrice = BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(cartItemDto.getQuantity()));
            cartItem.setTotalPrice(totalPrice.doubleValue());

            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);

        updateCartTotalPrice(cart);

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto removeItemFromCart(int userId, int cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Cart cart = user.getCart();
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for user id: " + userId);
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cart.getItems().contains(cartItem)) {
            throw new CartItemNotFoundException("Cart item id: " + cartItemId + " does not belong to user's cart");
        }

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        updateCartTotalPrice(cart);
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto updateItemQuantity(int userId, int cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Cart cart = user.getCart();
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for user id: " + userId);
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cart.getItems().contains(cartItem)) {
            throw new CartItemNotFoundException("Cart item id: " + cartItemId + " does not belong to user's cart");
        }

        cartItem.setQuantity(quantity);
        BigDecimal totalPrice = BigDecimal.valueOf(cartItem.getProduct().getPrice())
                .multiply(BigDecimal.valueOf(quantity));
        cartItem.setTotalPrice(totalPrice.doubleValue());

        cartItemRepository.save(cartItem);

        updateCartTotalPrice(cart);
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto getCartByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for user id: " + userId);
        }

        return modelMapper.map(cart, CartDto.class);
    }

    // Helper method to calculate and update the total price of the cart
    private void updateCartTotalPrice(Cart cart) {
        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty()) {
            cart.setTotalPrice(0.0);
            return;
        }

        double totalPrice = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        cart.setTotalPrice(totalPrice);
    }
}
