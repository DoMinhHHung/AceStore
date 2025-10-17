package iuh.fit.se.ace_store.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class   CartItemResponse {
    private Long productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
}
