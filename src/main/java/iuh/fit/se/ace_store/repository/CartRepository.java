package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Cart;
import iuh.fit.se.ace_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User userId);
    Optional<Cart> findByUserId(Long userId);
}
