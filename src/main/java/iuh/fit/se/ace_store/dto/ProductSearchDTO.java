package iuh.fit.se.ace_store.dto;

import lombok.Data;

@Data
public class ProductSearchDTO {
    private String keyword;      // Từ khóa tìm kiếm
    private String category;     // Loại sản phẩm
    private Double minPrice;     // Giá tối thiểu
    private Double maxPrice;     // Giá tối đa
    private Integer page;        // Trang hiện tại (phân trang)
    private Integer size;        // Số lượng sản phẩm mỗi trang
    private String sortBy;       // Sắp xếp theo trường nào (vd: price, name)
    private String sortDir;      // Thứ tự sắp xếp (asc/desc)
}
