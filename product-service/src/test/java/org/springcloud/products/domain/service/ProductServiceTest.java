package org.springcloud.products.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springcloud.products.domain.model.dto.request.ProductRequest;
import org.springcloud.products.domain.model.entity.Product;
import org.springcloud.products.infrastructure.persistence.ProductRepository;
import org.springcloud.products.infrastructure.utils.MappingUtils;

import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private ProductRequest mockProductRequest;
    private UUID randomUUID;

    @BeforeEach
    void setUp() {
        mockProduct = new Product(null, "Play Station 5", "Digital", 1200L);
        mockProductRequest = new ProductRequest("Play Station 5", "Digital", 1200L);
        randomUUID = UUID.randomUUID();
    }

    @DisplayName("Find one product")
    @Test
    void findOne() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> productService.findOne(randomUUID),
                "Debería lanzar la excepción");

        final UUID newId = UUID.randomUUID();
        when(productRepository.findById(any(UUID.class))).then((Answer<Optional<Product>>) invocation -> {
            mockProduct.setUuid(newId);
            return Optional.of(mockProduct);
        });
        var result = productService.findOne(newId);
        assertAll(
                () -> assertEquals(newId, result.getUuid()),
                () -> assertEquals(mockProduct, result, "Deberían ser iguales"),
                () -> assertDoesNotThrow(() -> NoSuchElementException.class, "No debería lanzar excepción"));

        verify(productRepository, times(2)).findById(any(UUID.class));
    }

    @DisplayName("Find all product list")
    @Test
    void findAll() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        Executable ex = () -> productService.findAll();

        var exception = assertThrows(NoSuchElementException.class, ex, "Debería lanzar una excepción");
        assertEquals("The list of products is empty", exception.getMessage());

        when(productRepository.findAll()).thenReturn(List.of(mockProduct));

        assertDoesNotThrow(ex);

        verify(productRepository, times(2)).findAll();
    }

    @DisplayName("Create product")
    @Test
    void create() {
        var mockNewProduct = MappingUtils.mapToClass(mockProductRequest, Product.class);
        when(productRepository.save(mockNewProduct)).thenReturn(mockProduct);

        Executable ex = () -> productService.create(mockProductRequest);

        assertAll(() -> assertDoesNotThrow(ex, "No debería lanzar excepción"),
                () -> assertNotNull(mockProduct)
        );

        Executable executable = () -> productService.create(null);

        assertThrowsExactly(InvalidParameterException.class, executable, "Debería lanzar excepción por nulo");

        verify(productRepository).save(any(Product.class));
    }

    @DisplayName("edit some product")
    @Test
    void edit() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Executable ex = () -> productService.edit(mockProductRequest, randomUUID);
        assertThrowsExactly(NoSuchElementException.class,
                ex,
                "Lanza excepción porque no encontró productos con ese UUID");

        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(any(Product.class));

        ex = () -> productService.edit(mockProductRequest, randomUUID);

        assertDoesNotThrow(ex, "No debe lanzar ninguna excepción");

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Delete product")
    @Test
    void delete() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockProduct));
        doNothing().when(productRepository).delete(any(Product.class));
        Executable ex = () -> productService.delete(randomUUID);

        assertDoesNotThrow(ex, "No debería lanzar excepción");
    }
}