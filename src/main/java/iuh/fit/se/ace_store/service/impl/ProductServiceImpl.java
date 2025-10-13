package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductDTO createProduct(ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        return null;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return List.of();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
