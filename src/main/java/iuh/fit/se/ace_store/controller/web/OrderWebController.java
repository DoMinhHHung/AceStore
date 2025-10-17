package iuh.fit.se.ace_store.controller.web;

import iuh.fit.se.ace_store.dto.request.OrderRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class OrderWebController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @GetMapping
    public String checkout(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        model.addAttribute("pageTitle", "Thanh toán");
        return "checkout";
    }

    @PostMapping
    public String placeOrder(Principal principal, @ModelAttribute OrderRequest req, Model model) {
        if (principal == null) return "redirect:/login";
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) return "redirect:/login";
        ApiResponse resp = orderService.createOrder(userOpt.get().getId(), req);
        if (resp.isSuccess()) {
            return "redirect:/orders";
        } else {
            model.addAttribute("error", resp.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Principal principal, Model model) {
        model.addAttribute("pageTitle", "Đơn hàng của tôi");
        return "orders";
    }
}