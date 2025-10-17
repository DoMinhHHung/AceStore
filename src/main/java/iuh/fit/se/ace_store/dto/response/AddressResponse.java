package iuh.fit.se.ace_store.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {
    private Long id;
    private String recipientName;
    private String phone;
    private String fullAddress;
    private boolean isDefault;
}
