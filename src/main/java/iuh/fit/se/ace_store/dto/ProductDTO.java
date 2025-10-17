package iuh.fit.se.ace_store.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private java.math.BigDecimal price;
    private String category;
    private String description;

    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String mainboard;
    private String psu;
    private String monitor;
    private String os;

    private List<String> images;
    private List<String> videos;
}
