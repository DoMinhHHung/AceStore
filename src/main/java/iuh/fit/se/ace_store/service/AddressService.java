package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.request.AddressRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;

public interface AddressService {
    public ApiResponse addAddress(String email, AddressRequest req);
    public ApiResponse getAddresses(String email);
    public ApiResponse deleteAddress(Long id, String email);
    public ApiResponse setDefaultAddress(Long addressId, String email);
}
