package iuh.fit.se.ace_store.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}
