
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
            return ResponseEntity.ok(new ApiResponse(true, null, "Tìm kiếm sản phẩm thành công!", null, result));
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
        try {
            ProductDTO result = productService.createProduct(productDTO, images, videos);
            return ResponseEntity.ok(new iuh.fit.se.ace_store.dto.response.ApiResponse(true, null, "Tạo sản phẩm thành công!", null, result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new iuh.fit.se.ace_store.dto.response.ApiResponse(false, "PRODUCT_CREATE_ERROR", e.getMessage(), "Kiểm tra lại thông tin sản phẩm.", null));
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            ProductDTO result = productService.updateProduct(id, productDTO, images, videos);
            return ResponseEntity.ok(new iuh.fit.se.ace_store.dto.response.ApiResponse(true, null, "Cập nhật sản phẩm thành công!", null, result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new iuh.fit.se.ace_store.dto.response.ApiResponse(false, "PRODUCT_UPDATE_ERROR", e.getMessage(), "Kiểm tra lại thông tin sản phẩm.", null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<ProductDTO> result = productService.getAllProducts();
            return ResponseEntity.ok(new iuh.fit.se.ace_store.dto.response.ApiResponse(true, null, "Lấy danh sách sản phẩm thành công!", null, result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new iuh.fit.se.ace_store.dto.response.ApiResponse(false, "PRODUCT_LIST_ERROR", e.getMessage(), null, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            ProductDTO result = productService.getProductById(id);
            return ResponseEntity.ok(new iuh.fit.se.ace_store.dto.response.ApiResponse(true, null, "Lấy sản phẩm thành công!", null, result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new iuh.fit.se.ace_store.dto.response.ApiResponse(false, "PRODUCT_GET_ERROR", e.getMessage(), null, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new iuh.fit.se.ace_store.dto.response.ApiResponse(true, null, "Xóa sản phẩm thành công!", null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new iuh.fit.se.ace_store.dto.response.ApiResponse(false, "PRODUCT_DELETE_ERROR", e.getMessage(), null, null));
        }
    }
}
