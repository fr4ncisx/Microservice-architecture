package org.springcloud.products.infrastructure.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springcloud.products.domain.model.dto.request.ProductRequest;
import org.springcloud.products.domain.model.entity.Product;
import org.springcloud.products.domain.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("{uuid}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID uuid){
        return ResponseEntity.ok(productService.findOne(uuid));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(productRequest));
    }

    @PutMapping("{uuid}")
    public ResponseEntity<Void> edit(@RequestBody @Valid ProductRequest productRequest, @PathVariable UUID uuid){
        productService.edit(productRequest, uuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid){
        productService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}
