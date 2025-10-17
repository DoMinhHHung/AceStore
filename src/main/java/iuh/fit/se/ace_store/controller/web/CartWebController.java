package iuh.fit.se.ace_store.controller.web;

import iuh.fit.se.ace_store.dto.request.CartRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartWebController {
    private final CartService cartService;

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        if (principal != null) {
            ApiResponse resp = cartService.getCart(principal.getName());
            model.addAttribute("cartResp", resp);
        }
        model.addAttribute("pageTitle", "Giỏ hàng");
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity, Principal principal) {
        if (principal == null) return "redirect:/login";
        CartRequest req = new CartRequest();
        try {
            req.setProductId(productId);
            req.setQuantity(quantity);
        } catch (Exception ex) {
            // ignore: adapt if DTO differs
        }
        cartService.addToCart(principal.getName(), req);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam Long productId, @RequestParam int quantity, Principal principal) {
        if (principal == null) return "redirect:/login";
        cartService.updateCartItem(principal.getName(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long productId, Principal principal) {
        if (principal == null) return "redirect:/login";
        cartService.removeCartItem(principal.getName(), productId);
        return "redirect:/cart";
    }
}