package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.ProductSearchRequest;
import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ace/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchProducts(@RequestBody ProductSearchRequest searchDTO) {
        try {
            List<ProductDTO> result = productService.searchProducts(searchDTO);
            return ResponseEntity.ok(new ApiResponse(true, null, "Product search successful", null, result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "PRODUCT_SEARCH_ERROR", e.getMessage(), null, null));
        }
    }
    private final ProductService productService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> createProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        ApiResponse result = productService.createProduct(productDTO, images, videos);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        ApiResponse result = productService.updateProduct(id, productDTO, images, videos);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        ApiResponse result = productService.getAllProducts();
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ApiResponse result = productService.getProductById(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        ApiResponse result = productService.deleteProduct(id);
        if (result.isSuccess()) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }
}
