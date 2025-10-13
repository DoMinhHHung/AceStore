package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
