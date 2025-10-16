package iuh.fit.se.ace_store.dto.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private int quantity;
}
