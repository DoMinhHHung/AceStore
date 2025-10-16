package iuh.fit.se.ace_store.mapper;

import iuh.fit.se.ace_store.dto.response.CartItemResponse;
import iuh.fit.se.ace_store.dto.response.CartResponse;
import iuh.fit.se.ace_store.entity.Cart;
import iuh.fit.se.ace_store.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .imageUrl(item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty()
                ? item.getProduct().getImages().get(0): null)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    public CartResponse toCartResponse(Cart cart) {
        if(cart == null) return null;

        return CartResponse.builder()
                .cartId(cart.getId())
                .totalPrice((cart.getTotalPrice()))
                .items(cart.getItems().stream()
                        .map(this::toCartItemResponse)
                        .collect(Collectors.toList()))
                .build();

    }
}
