package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Cart;
import iuh.fit.se.ace_store.entity.CartItem;
import iuh.fit.se.ace_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
    void deleteByCartAndProductId(Cart cart, Long productId);
}
