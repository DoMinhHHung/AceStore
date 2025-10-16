package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository  extends JpaRepository<Address, Long> {
    List<Address> findByUserIdOrderByIdDesc(Long userId);
}
