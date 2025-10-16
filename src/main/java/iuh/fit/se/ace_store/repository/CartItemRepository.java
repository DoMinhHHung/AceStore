package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
