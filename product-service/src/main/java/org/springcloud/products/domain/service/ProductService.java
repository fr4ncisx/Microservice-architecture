package org.springcloud.products.domain.service;

import lombok.RequiredArgsConstructor;
import org.springcloud.products.domain.model.dto.request.ProductRequest;
import org.springcloud.products.domain.model.entity.Product;
import org.springcloud.products.infrastructure.persistence.ProductRepository;
import org.springcloud.products.infrastructure.utils.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product findOne(UUID uuid){
        return getProduct(uuid);
    }

    public List<Product> findAll(){
        var productList = productRepository.findAll();
        if(productList.isEmpty())
            throw new NoSuchElementException("The list of products is empty");
        return productList;
    }

    @Transactional
    public Product create(ProductRequest productRequest){
        if(productRequest != null) {
            Product product = MappingUtils.mapToClass(productRequest, Product.class);
            return productRepository.save(product);
        }
        throw new InvalidParameterException("Product must not be null");
    }

    @Transactional
    public void edit(ProductRequest productRequest, UUID uuid){
        var product = getProduct(uuid);
        MappingUtils.mapToExistingInstance(productRequest, product);
        productRepository.save(product);
    }

    @Transactional
    public void delete(UUID uuid){
        var product = getProduct(uuid);
        productRepository.delete(product);
    }

    private Product getProduct(UUID uuid){
        return productRepository.findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Product was not found"));
    }
}
