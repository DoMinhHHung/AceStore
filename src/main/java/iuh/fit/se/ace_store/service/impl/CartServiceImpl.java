package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.repository.CartItemRepository;
import iuh.fit.se.ace_store.repository.CartRepository;
import iuh.fit.se.ace_store.repository.ProductRepository;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.CartService;
import lombok.RequiredArgsConstructor;
import iuh.fit.se.ace_store.mapper.CartMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository  userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository  cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    public ApiResponse addToCart(Long userId, CartRequest request) {
        return null;
    }

    @Override
    public ApiResponse getCart(Long userId) {
        return null;
    }

    @Override
    public ApiResponse updateCartItem(Long userId, Long productId, int quantity) {
        return null;
    }

    @Override
    public ApiResponse removeCartItem(Long userId, Long productId) {
        return null;
    }

    @Override
    public ApiResponse clearCart(Long userId) {
        return null;
    }
}
