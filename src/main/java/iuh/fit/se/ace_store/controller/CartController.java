package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ace/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ApiResponse addToCart(@PathVariable Long userId, @RequestBody CartRequest request) {
        return cartService.addToCart(userId, request);
    }

    @GetMapping("/{userId}")
    public ApiResponse getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PutMapping("/{userId}/update/{productId}")
    public ApiResponse updateCartItem(@PathVariable Long userId,
                                      @PathVariable Long productId,
                                      @RequestParam int quantity) {
        return cartService.updateCartItem(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ApiResponse removeCartItem(@PathVariable Long userId,
                                      @PathVariable Long productId) {
        return cartService.removeCartItem(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public ApiResponse clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}
