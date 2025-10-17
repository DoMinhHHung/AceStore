package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.config.cloudinary.CloudinaryService;
import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.Product;
import iuh.fit.se.ace_store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, cloudinaryService);
    }

    @Test
    void createProduct_whenUploadImages_setsMainImageToFirstUploaded() throws Exception {
        // Arrange
        ProductDTO dto = ProductDTO.builder()
                .name("Test")
                .brand("Brand")
                .price(BigDecimal.valueOf(1000))
                .stock(5)
                .build();

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        List<MultipartFile> images = Arrays.asList(file1, file2);

        List<String> uploadedUrls = Arrays.asList("http://cloud/img1.jpg", "http://cloud/img2.jpg");
        when(cloudinaryService.uploadFiles(eq(images), eq("image"))).thenReturn(uploadedUrls);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        // Act
        ApiResponse resp = productService.createProduct(dto, images, null);

        // Assert
        assertTrue(resp.isSuccess());
        assertNotNull(resp.getData());
        assertTrue(resp.getData() instanceof ProductDTO);
        ProductDTO result = (ProductDTO) resp.getData();
        assertEquals(10L, result.getId());
        assertEquals(uploadedUrls, result.getImages());
        assertEquals(uploadedUrls.get(0), result.getMainImage());
    }

    @Test
    void createProduct_whenNoImagesAndDtoHasNoImages_mainImageEmpty() {
        // Arrange
        ProductDTO dto = ProductDTO.builder()
                .name("NoImage")
                .price(BigDecimal.ONE)
                .stock(1)
                .build();

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(11L);
            return p;
        });

        // Act
        ApiResponse resp = productService.createProduct(dto, null, null);

        // Assert
        assertTrue(resp.isSuccess());
        ProductDTO result = (ProductDTO) resp.getData();
        assertEquals(11L, result.getId());
        assertNotNull(result.getImages());
        assertTrue(result.getImages().isEmpty());
        assertNotNull(result.getMainImage());
        assertEquals("", result.getMainImage());
    }

    @Test
    void updateProduct_whenNoNewImages_preserveOldMainImage() {
        // Arrange
        Long id = 20L;
        Product existing = Product.builder()
                .id(id)
                .name("Existing")
                .images(Arrays.asList("http://old/1.jpg", "http://old/2.jpg"))
                .mainImage("http://old/1.jpg")
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO dto = ProductDTO.builder()
                .name("Updated")
                .build();

        // Act
        ApiResponse resp = productService.updateProduct(id, dto, null, null);

        // Assert
        assertTrue(resp.isSuccess());
        ProductDTO result = (ProductDTO) resp.getData();
        assertEquals(existing.getImages(), result.getImages());
        assertEquals(existing.getMainImage(), result.getMainImage());
    }

    @Test
    void updateProduct_whenUploadNewImages_replaceImagesAndMainImage() throws Exception {
        // Arrange
        Long id = 21L;
        Product existing = Product.builder()
                .id(id)
                .name("Existing")
                .images(Arrays.asList("http://old/1.jpg"))
                .mainImage("http://old/1.jpg")
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MultipartFile n1 = mock(MultipartFile.class);
        List<MultipartFile> newImages = List.of(n1);
        List<String> newUrls = List.of("http://new/1.jpg");
        when(cloudinaryService.uploadFiles(eq(newImages), eq("image"))).thenReturn(newUrls);

        ProductDTO dto = ProductDTO.builder().name("Updated").build();

        // Act
        ApiResponse resp = productService.updateProduct(id, dto, newImages, null);

        // Assert
        assertTrue(resp.isSuccess());
        ProductDTO result = (ProductDTO) resp.getData();
        assertEquals(newUrls, result.getImages());
        assertEquals(newUrls.get(0), result.getMainImage());
    }
}
