package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.OrderRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.OrderService;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ace/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping
    public ApiResponse createOrder(java.security.Principal principal, @RequestBody OrderRequest request) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return orderService.createOrder(user.getId(), request);
    }
}
