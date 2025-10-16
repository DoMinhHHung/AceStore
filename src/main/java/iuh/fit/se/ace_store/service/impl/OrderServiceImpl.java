package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.OrderRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.repository.*;
import iuh.fit.se.ace_store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse createOrder(Long userId, OrderRequest request) {
        return null;
    }
}
