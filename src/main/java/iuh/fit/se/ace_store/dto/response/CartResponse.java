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
    private Double totalPrice;
    private List<CartItemResponse> items;
}
