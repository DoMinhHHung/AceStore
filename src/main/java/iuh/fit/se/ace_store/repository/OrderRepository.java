package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
