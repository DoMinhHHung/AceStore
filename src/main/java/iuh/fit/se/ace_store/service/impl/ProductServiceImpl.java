package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.config.cloudinary.CloudinaryService;
import iuh.fit.se.ace_store.dto.ProductDTO;
import iuh.fit.se.ace_store.entity.Product;
import iuh.fit.se.ace_store.repository.ProductRepository;
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
    public List<ProductDTO> searchProducts(iuh.fit.se.ace_store.dto.ProductSearchDTO searchDTO) {
        return productRepository.findAll().stream()
                .filter(p -> searchDTO.getKeyword() == null || p.getName().toLowerCase().contains(searchDTO.getKeyword().toLowerCase()))
                .filter(p -> searchDTO.getCategory() == null || p.getCategory().equalsIgnoreCase(searchDTO.getCategory()))
                .filter(p -> searchDTO.getMinPrice() == null || p.getPrice() >= searchDTO.getMinPrice())
                .filter(p -> searchDTO.getMaxPrice() == null || p.getPrice() <= searchDTO.getMaxPrice())
                .map(this::toDTO)
                .toList();
    }
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ProductDTO createProduct(ProductDTO dto, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        List<String> imageUrls = cloudinaryService.uploadFiles(images, "image");
        List<String> videoUrls = cloudinaryService.uploadFiles(videos, "video");

        Product product = Product.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
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
                .images(imageUrls)
                .videos(videoUrls)
                .build();

        Product saved = productRepository.save(product);
        dto.setId(saved.getId());
        dto.setImages(imageUrls);
        dto.setVideos(videoUrls);
        return dto;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto, List<MultipartFile> images, List<MultipartFile> videos) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (images != null && !images.isEmpty()) {
            product.setImages(cloudinaryService.uploadFiles(images, "image"));
        }
        if (videos != null && !videos.isEmpty()) {
            product.setVideos(cloudinaryService.uploadFiles(videos, "video"));
        }

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setCpu(dto.getCpu());
        product.setRam(dto.getRam());
        product.setStorage(dto.getStorage());
        product.setGpu(dto.getGpu());
        product.setMainboard(dto.getMainboard());
        product.setPsu(dto.getPsu());
        product.setMonitor(dto.getMonitor());
        product.setOs(dto.getOs());

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = cloudinaryService.uploadFiles(images, "image");
            product.setImages(imageUrls);
        }

        if (videos != null && !videos.isEmpty()) {
            List<String> videoUrls = cloudinaryService.uploadFiles(videos, "video");
            product.setVideos(videoUrls);
        }

        Product updated = productRepository.save(product);
        return dto;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .brand(p.getBrand())
                .price(p.getPrice())
                .category(p.getCategory())
                .description(p.getDescription())
                .cpu(p.getCpu())
                .ram(p.getRam())
                .storage(p.getStorage())
                .gpu(p.getGpu())
                .mainboard(p.getMainboard())
                .psu(p.getPsu())
                .monitor(p.getMonitor())
                .os(p.getOs())
                .images(p.getImages())
                .videos(p.getVideos())
                .build();
    }
}
