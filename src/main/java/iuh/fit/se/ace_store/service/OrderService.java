package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.request.OrderRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;

public interface OrderService {
    ApiResponse createOrder(Long userId, OrderRequest request);
}
