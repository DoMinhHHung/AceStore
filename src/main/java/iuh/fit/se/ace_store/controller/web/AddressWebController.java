package iuh.fit.se.ace_store.controller.web;

import iuh.fit.se.ace_store.dto.request.AddressRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressWebController {
    private final AddressService addressService;

    @GetMapping
    public String addresses(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        ApiResponse resp = addressService.getAddresses(principal.getName());
        model.addAttribute("addressesResp", resp);
        model.addAttribute("pageTitle", "Địa chỉ của tôi");
        return "addresses";
    }

    @PostMapping("/add")
    public String addAddress(Principal principal, @ModelAttribute AddressRequest req) {
        if (principal == null) return "redirect:/login";
        addressService.addAddress(principal.getName(), req);
        return "redirect:/addresses";
    }

    @PostMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        addressService.deleteAddress(id, principal.getName());
        return "redirect:/addresses";
    }

    @PostMapping("/{id}/default")
    public String setDefault(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        addressService.setDefaultAddress(id, principal.getName());
        return "redirect:/addresses";
    }
}