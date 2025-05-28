package org.springcloud.users.domain.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springcloud.users.domain.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private UUID id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private List<UserProduct> userProductList;
    @Transient
    private List<Product> productList = new ArrayList<>();

}
