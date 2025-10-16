package iuh.fit.se.ace_store.entity;

import iuh.fit.se.ace_store.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private boolean isPickup;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private LocalDateTime createdAt;
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
