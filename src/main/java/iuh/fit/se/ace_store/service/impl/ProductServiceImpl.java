package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.config.cloudinary.CloudinaryService;
import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.dto.request.ProductSearchRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.Product;
import iuh.fit.se.ace_store.repository.ProductRepository;
import iuh.fit.se.ace_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<ProductDTO> searchProducts(ProductSearchRequest searchDTO) {
        return productRepository.findAll().stream()
                .filter(p -> searchDTO.getKeyword() == null || p.getName().toLowerCase().contains(searchDTO.getKeyword().toLowerCase()))
                .filter(p -> searchDTO.getCategory() == null || p.getCategory().equalsIgnoreCase(searchDTO.getCategory()))
                .filter(p -> searchDTO.getMinPrice() == null || p.getPrice().compareTo(searchDTO.getMinPrice()) >= 0)
                .filter(p -> searchDTO.getMaxPrice() == null || p.getPrice().compareTo(searchDTO.getMaxPrice()) <= 0)
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ApiResponse createProduct(ProductDTO dto, List<MultipartFile> images, List<MultipartFile> videos) {
        List<String> imageUrls = new ArrayList<>();
        List<String> videoUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            try {
                imageUrls = cloudinaryService.uploadFiles(images, "image");
            } catch (IOException e) {
                return ApiResponse.error("Failed to upload images: " + e.getMessage(), "CLOUDINARY_UPLOAD_ERROR");
            }
        } else if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            imageUrls = dto.getImages();
        }

        if (videos != null && !videos.isEmpty()) {
            try {
                videoUrls = cloudinaryService.uploadFiles(videos, "video");
            } catch (IOException e) {
                return ApiResponse.error("Failed to upload videos: " + e.getMessage(), "CLOUDINARY_UPLOAD_ERROR");
            }
        } else if (dto.getVideos() != null && !dto.getVideos().isEmpty()) {
            videoUrls = dto.getVideos();
        }

        String mainImage = (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;

        Product product = Product.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .cpu(dto.getCpu())
                .ram(dto.getRam())
                .storage(dto.getStorage())
                .gpu(dto.getGpu())
                .mainboard(dto.getMainboard())
                .psu(dto.getPsu())
                .monitor(dto.getMonitor())
                .os(dto.getOs())
                .mainImage(mainImage)
                .images(imageUrls)
                .videos(videoUrls)
                .build();

        Product saved = productRepository.save(product);
        ProductDTO result = toDTO(saved);
        return ApiResponse.success("Product created successfully", result);
    }

    @Override
    public ApiResponse updateProduct(Long id, ProductDTO dto, List<MultipartFile> images, List<MultipartFile> videos) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return ApiResponse.error("Product not found", "PRODUCT_NOT_FOUND");
        }
        Product product = opt.get();

        // Handle images: upload new if provided, otherwise keep existing or use dto.images if provided
        if (images != null && !images.isEmpty()) {
            try {
                List<String> uploaded = cloudinaryService.uploadFiles(images, "image");
                product.setImages(uploaded);
                product.setMainImage(uploaded.isEmpty() ? null : uploaded.get(0));
            } catch (IOException e) {
                return ApiResponse.error("Failed to upload images: " + e.getMessage(), "CLOUDINARY_UPLOAD_ERROR");
            }
        } else if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            product.setImages(dto.getImages());
            product.setMainImage(dto.getImages().isEmpty() ? null : dto.getImages().get(0));
        }

        if (videos != null && !videos.isEmpty()) {
            try {
                List<String> uploadedV = cloudinaryService.uploadFiles(videos, "video");
                product.setVideos(uploadedV);
            } catch (IOException e) {
                return ApiResponse.error("Failed to upload videos: " + e.getMessage(), "CLOUDINARY_UPLOAD_ERROR");
            }
        } else if (dto.getVideos() != null && !dto.getVideos().isEmpty()) {
            product.setVideos(dto.getVideos());
        }

        if (dto.getName() != null && !dto.getName().isBlank()) product.setName(dto.getName());
        if (dto.getBrand() != null && !dto.getBrand().isBlank()) product.setBrand(dto.getBrand());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getStock() != null) product.setStock(dto.getStock());
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) product.setCategory(dto.getCategory());
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) product.setDescription(dto.getDescription());
        if (dto.getCpu() != null && !dto.getCpu().isBlank()) product.setCpu(dto.getCpu());
        if (dto.getRam() != null && !dto.getRam().isBlank()) product.setRam(dto.getRam());
        if (dto.getStorage() != null && !dto.getStorage().isBlank()) product.setStorage(dto.getStorage());
        if (dto.getGpu() != null && !dto.getGpu().isBlank()) product.setGpu(dto.getGpu());
        if (dto.getMainboard() != null && !dto.getMainboard().isBlank()) product.setMainboard(dto.getMainboard());
        if (dto.getPsu() != null && !dto.getPsu().isBlank()) product.setPsu(dto.getPsu());
        if (dto.getMonitor() != null && !dto.getMonitor().isBlank()) product.setMonitor(dto.getMonitor());
        if (dto.getOs() != null && !dto.getOs().isBlank()) product.setOs(dto.getOs());

        Product updated = productRepository.save(product);
        ProductDTO result = toDTO(updated);
        return ApiResponse.success("Product updated successfully", result);
    }

    @Override
    public ApiResponse getAllProducts() {
        List<ProductDTO> list = productRepository.findAll().stream().map(this::toDTO).toList();
        return ApiResponse.success("Retrieved product list successfully", list);
    }

    @Override
    public ApiResponse getProductById(Long id) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.error("Product not found", "PRODUCT_NOT_FOUND");
        return ApiResponse.success(toDTO(opt.get()));
    }

    @Override
    public ApiResponse deleteProduct(Long id) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) return ApiResponse.error("Product not found", "PRODUCT_NOT_FOUND");
        productRepository.deleteById(id);
        return ApiResponse.success("Product deleted successfully");
    }

    private ProductDTO toDTO(Product p) {
        List<String> images = p.getImages() != null ? p.getImages() : new ArrayList<>();
        List<String> videos = p.getVideos() != null ? p.getVideos() : new ArrayList<>();
        String mainImage = p.getMainImage();
        if ((mainImage == null || mainImage.isBlank()) && !images.isEmpty()) {
            mainImage = images.get(0);
        }

        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName() != null ? p.getName() : null)
                .brand(p.getBrand() != null ? p.getBrand() : null)
                .price(p.getPrice() != null ? p.getPrice() : null)
                .stock(p.getStock() != null ? p.getStock() : null)
                .category(p.getCategory() != null ? p.getCategory() : null)
                .description(p.getDescription() != null ? p.getDescription() : null)
                .cpu(p.getCpu() != null ? p.getCpu() : null)
                .ram(p.getRam() != null ? p.getRam() : null)
                .storage(p.getStorage() != null ? p.getStorage() : null)
                .gpu(p.getGpu() != null ? p.getGpu() : null)
                .mainboard(p.getMainboard() != null ? p.getMainboard() : null)
                .psu(p.getPsu() != null ? p.getPsu() : null)
                .monitor(p.getMonitor() != null ? p.getMonitor() : null)
                .os(p.getOs() != null ? p.getOs() : null)
                .mainImage(mainImage != null ? mainImage : null)
                .images(images)
                .videos(videos)
                .build();
    }
}
