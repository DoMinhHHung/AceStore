package iuh.fit.se.ace_store.dto.response;

import iuh.fit.se.ace_store.entity.CartItem;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private java.math.BigDecimal totalPrice;
    private List<CartItemResponse> items;
}
