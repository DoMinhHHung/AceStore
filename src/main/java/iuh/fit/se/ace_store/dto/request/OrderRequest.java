package iuh.fit.se.ace_store.dto.request;

import iuh.fit.se.ace_store.entity.enums.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    private boolean isPickup;
    private Long addressId;
    private PaymentMethod  paymentMethod;
}
