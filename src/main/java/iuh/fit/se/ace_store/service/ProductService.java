package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos) throws IOException;
    ProductDTO updateProduct(Long id, ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos) throws IOException;
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    void deleteProduct(Long id);
}
