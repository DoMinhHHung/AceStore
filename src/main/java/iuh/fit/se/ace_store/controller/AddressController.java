package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.AddressRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/ace/user/addresses", "/ace/addresses"})
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ApiResponse addAddress(@AuthenticationPrincipal UserDetails user, @RequestBody AddressRequest req) {
        return addressService.addAddress(user.getUsername(), req);
    }

    @GetMapping
    public ApiResponse getAddresses(@AuthenticationPrincipal UserDetails user) {
        return addressService.getAddresses(user.getUsername());
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteAddress(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return addressService.deleteAddress(id, user.getUsername());
    }
}
