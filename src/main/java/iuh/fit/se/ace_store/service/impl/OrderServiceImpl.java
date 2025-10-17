package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.OrderRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.*;
import iuh.fit.se.ace_store.repository.*;
import iuh.fit.se.ace_store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ApiResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found") );
        // Load cart and items via CartRepository to ensure we have the user's cart
        var cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            return ApiResponse.error("Cart is empty!");
        }
        var cart = cartOpt.get();
        var cartItems = cart.getItems();
        if (cartItems == null || cartItems.isEmpty()) {
            return ApiResponse.error("Cart is empty!");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getId()));
            if (item.getQuantity() > (product.getStock() == null ? 0 : product.getStock())) {
                return ApiResponse.error("Product " + product.getName() + " is out of stock!");
            }
            BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        Address address = null;
        if (!request.isPickup()) {
            if (request.getAddressId() != null) {
                address = addressRepository.findById(request.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Address not found"));
            } else {
                address = addressRepository.findByUserAndIsDefaultTrue(user)
                        .orElseThrow(() -> new RuntimeException("No default address found for user"));
            }
        }

        Order order = Order.builder()
                .user(user)
                .orderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
        .totalAmount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
        .pickup(request.isPickup())
                .address(address)
                .createdAt(LocalDateTime.now())
                .status("PENDING")
                .build();
    List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
        Product product = productRepository.findById(cartItem.getProduct().getId())
            .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProduct().getId()));
        // decrement stock
        product.setStock(product.getStock() - cartItem.getQuantity());
        productRepository.save(product);

    BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
        return OrderItem.builder()
            .order(order)
            .product(product)
            .quantity(cartItem.getQuantity())
            .price(price)
            .build();
    }).collect(Collectors.toList());

        order.setItems(orderItems);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return ApiResponse.success("Order created successfully!", order);
        
    }
}
