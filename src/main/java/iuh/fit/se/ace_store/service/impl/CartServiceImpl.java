package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.Cart;
import iuh.fit.se.ace_store.entity.CartItem;
import iuh.fit.se.ace_store.entity.Product;
import iuh.fit.se.ace_store.entity.User;
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
        public ApiResponse addToCart(String userEmail, CartRequest request) {
                User user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .user(user)
                        .totalPrice(java.math.BigDecimal.ZERO)
                        .build()));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    newItem.setPrice(product.getPrice());
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.getQuantity());
        item.setPrice(item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
        cartItemRepository.save(item);
        cart.setTotalPrice(cart.getItems().stream()
                .map(ci -> ci.getPrice() == null ? java.math.BigDecimal.ZERO : ci.getPrice())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        cartRepository.save(cart);

        return ApiResponse.success("Added to cart successfully", cartMapper.toCartResponse(cart));

    }

    @Override
        public ApiResponse getCart(String userEmail) {
                User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
                Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        return ApiResponse.success("Cart successfully", cartMapper.toCartResponse(cart));
    }

        @Override
        public ApiResponse updateCartItem(String userEmail, Long productId, int quantity) {
                User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
                Cart cart = cartRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem item = cartItemRepository.findByCartAndProductId(cart, productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(quantity);
        item.setPrice(item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(quantity)));
        cartItemRepository.save(item);

        cart.setTotalPrice(cart.getItems().stream()
                .map(ci -> ci.getPrice() == null ? java.math.BigDecimal.ZERO : ci.getPrice())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        cartRepository.save(cart);

        return ApiResponse.success("Cart updated", cartMapper.toCartResponse(cart));
    }

        @Override
        public ApiResponse removeCartItem(String userEmail, Long productId) {
                User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
                Cart cart = cartRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteByCartAndProductId(cart, productId);

        cart.setTotalPrice(cart.getItems().stream()
                .map(ci -> ci.getPrice() == null ? java.math.BigDecimal.ZERO : ci.getPrice())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        cartRepository.save(cart);

        return ApiResponse.success("Item removed", cartMapper.toCartResponse(cart));

    }

        @Override
        public ApiResponse clearCart(String userEmail) {
                User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
                Cart cart = cartRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Cart not found"));
                cartItemRepository.deleteAll(cart.getItems());
                cart.setTotalPrice(java.math.BigDecimal.ZERO);
                cartRepository.save(cart);
                return ApiResponse.success("Cart cleared", cartMapper.toCartResponse(cart));
        }
}
