package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    // The service methods now accept the authenticated user's email (from Principal)
    ApiResponse addToCart(String userEmail, CartRequest request);
    ApiResponse getCart(String userEmail);
    ApiResponse updateCartItem(String userEmail, Long productId, int quantity);
    ApiResponse removeCartItem(String userEmail, Long productId);
    ApiResponse clearCart(String userEmail);
}
