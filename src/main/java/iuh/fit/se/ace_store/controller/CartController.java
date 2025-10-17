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

    @PostMapping("/add")
    public ApiResponse addToCart(java.security.Principal principal, @RequestBody CartRequest request) {
        String email = principal.getName();
        return cartService.addToCart(email, request);
    }

    @GetMapping
    public ApiResponse getCart(java.security.Principal principal) {
        String email = principal.getName();
        return cartService.getCart(email);
    }

    @PutMapping("/update/{productId}")
    public ApiResponse updateCartItem(java.security.Principal principal,
                                      @PathVariable Long productId,
                                      @RequestParam int quantity) {
        String email = principal.getName();
        return cartService.updateCartItem(email, productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public ApiResponse removeCartItem(java.security.Principal principal,
                                      @PathVariable Long productId) {
        String email = principal.getName();
        return cartService.removeCartItem(email, productId);
    }

    @DeleteMapping("/clear")
    public ApiResponse clearCart(java.security.Principal principal) {
        String email = principal.getName();
        return cartService.clearCart(email);
    }
}
