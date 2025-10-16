package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.CartResponse;

public interface CartService {
    ApiResponse addToCart(Long userId, CartRequest request);
    ApiResponse getCart(Long userId);
    ApiResponse updateCartItem(Long userId, Long productId, int quantity);
    ApiResponse removeCartItem(Long userId, Long productId);
    ApiResponse clearCart(Long userId);
}
