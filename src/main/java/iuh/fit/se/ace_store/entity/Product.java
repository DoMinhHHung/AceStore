package iuh.fit.se.ace_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private Double price;

    private String category;
    @Column(length = 2000)
    private String description;

    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String mainboard;
    private String psu;
    private String monitor;
    private String os;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "product_videos", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "video_url")
    private List<String> videos;
}
