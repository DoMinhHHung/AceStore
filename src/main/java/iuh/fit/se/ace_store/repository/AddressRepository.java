package iuh.fit.se.ace_store.repository;

import iuh.fit.se.ace_store.entity.Address;
import iuh.fit.se.ace_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository  extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndIsDefaultTrue(User user);
}
