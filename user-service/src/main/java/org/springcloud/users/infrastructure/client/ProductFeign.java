package org.springcloud.users.infrastructure.client;

import org.springcloud.users.domain.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductFeign {

    @GetMapping("{uuid}")
    Product getProduct(@PathVariable UUID uuid);

    @PostMapping
    void createProduct(@RequestBody Product product);

}
