package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.AddressRequest;
import iuh.fit.se.ace_store.dto.response.AddressResponse;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.Address;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.AddressRepository;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse addAddress(String email, AddressRequest req) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(req.isDefault()){
            addressRepository.findByUser(user).forEach(a -> a.setDefault(false));
        }
        Address address = Address.builder()
                .recipientName(req.getRecipientName())
                .phone(req.getPhone())
                .province(req.getProvince())
                .district(req.getDistrict())
                .ward(req.getWard())
                .detail(req.getDetail())
                .isDefault(req.isDefault())
                .user(user)
                .build();
        addressRepository.save(address);
        return ApiResponse.success("New address has been saved successfully");
    }

    @Override
    public ApiResponse getAddresses(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<AddressResponse> list = addressRepository.findByUser(user)
                .stream()
                .map(a -> AddressResponse.builder()
                        .id(a.getId())
                        .recipientName(a.getRecipientName())
                        .phone(a.getPhone())
                        .fullAddress(a.getDetail()+", " + a.getWard()+", "+a.getDistrict()+", "+a.getProvince())
                        .isDefault(a.isDefault())
                        .build())
                .collect(Collectors.toList());
        return  ApiResponse.success(list);
    }

    @Override
    public ApiResponse deleteAddress(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        if(!address.getUser().getId().equals(user.getId())){
            return ApiResponse.error("Delete address failed");
        }
        addressRepository.delete(address);
        return ApiResponse.success("Address has been deleted successfully");
    }

    @Override
    @Transactional
    public ApiResponse setDefaultAddress(Long addressId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Address not found"));
        if(!address.getUser().equals(user)){
            throw new RuntimeException("Address does not belong to user!");
        }
        addressRepository.findByUser(user).forEach(a -> {
            if (a.isDefault()) a.setDefault(false);
        });
        address.setDefault(true);
        addressRepository.save(address);
        return ApiResponse.success("Address has been saved successfully");
    }
}
