package iuh.fit.se.ace_store.dto.request;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private String keyword;
    private String category;
    private Double minPrice;
    private Double maxPrice;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDir;
}
