package iuh.fit.se.ace_store.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String recipientName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String detail;
    private boolean isDefault;
}
