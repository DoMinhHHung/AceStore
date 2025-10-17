package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;
import iuh.fit.se.ace_store.dto.request.ProductSearchRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;

import java.util.List;

public interface ProductService {
    List<ProductDTO> searchProducts(ProductSearchRequest searchDTO);

    ApiResponse createProduct(ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos);

    ApiResponse updateProduct(Long id, ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos);

    ApiResponse getAllProducts();

    ApiResponse getProductById(Long id);

    ApiResponse deleteProduct(Long id);
}
