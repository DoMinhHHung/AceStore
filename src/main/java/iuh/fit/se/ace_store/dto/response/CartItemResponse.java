package iuh.fit.se.ace_store.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private Double price;
}
