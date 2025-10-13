package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) throws IOException {
        return ResponseEntity.ok(productService.createProduct(productDTO, images, videos));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) throws IOException {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO, images, videos));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
