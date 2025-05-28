package org.springcloud.users.domain.service;

import lombok.RequiredArgsConstructor;
import org.springcloud.users.domain.models.Product;
import org.springcloud.users.domain.models.dto.request.UserRequest;
import org.springcloud.users.domain.models.dto.response.UserResponse;
import org.springcloud.users.domain.models.entity.User;
import org.springcloud.users.domain.models.entity.UserProduct;
import org.springcloud.users.infrastructure.client.ProductFeign;
import org.springcloud.users.infrastructure.persistence.repository.UserRepository;

import static org.springcloud.users.utils.MappingUtils.*;
import static org.springcloud.users.utils.ValidationUtils.assertUserNotNull;

import org.springcloud.users.utils.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductFeign productFeign;

    public List<UserResponse> findAll() {
        var userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }
        Product productClient = null;
        for (var user : userList) {
            for (var product : user.getUserProductList()) {
                productClient = productFeign.getProduct(product.getProductId());
            }
            if (productClient != null)
                user.getProductList().add(productClient);
        }
        return userList.stream()
                .map(MappingUtils::returnUserResponse)
                .toList();
    }

    public UserResponse findById(UUID uuid) {
        return getUserResponseById(uuid);
    }

    @Transactional
    public UserResponse create(UserRequest userRequest) {
        userRequest = assertUserNotNull(userRequest, () -> new IllegalArgumentException("El usuario no puede ser nulo"));
        var user = userRepository.save(mapToClass(userRequest, User.class));
        return mapToClass(user, UserResponse.class);
    }

    @Transactional
    public Map<String, String> update(UUID uuid, UserRequest userRequest) {
        var user = getUserById(uuid);
        mapToExistingInstance(userRequest, user);
        userRepository.save(user);
        return Map.of("stats", "Los cambios se han actualizado exitosamente");
    }

    @Transactional
    public Map<String, String> delete(UUID userId) {
        var user = getUserById(userId);
        userRepository.delete(user);
        return Map.of("stats", "El usuario ha sido eliminado");
    }

    @Transactional
    public UserResponse addProductToUser(UUID productId, UUID userId) {
        var user = getUserById(userId);
        var prod = productFeign.getProduct(productId);
        UserProduct userProduct = new UserProduct();
        userProduct.setProductId(productId);
        user.getProductList().add(prod);
        user.getUserProductList().add(userProduct);
        return MappingUtils.mapToClass(userRepository.save(user), UserResponse.class);
    }

    public void deleteProductFromUser(UUID productId, UUID userId) {
        User user = getUserById(userId);
        var isRemoved = user.getUserProductList()
                .removeIf(p -> p.getProductId().equals(productId));
        if(!isRemoved)
            throw new IllegalArgumentException("Product was not found");
        userRepository.save(user);
    }

    private UserResponse getUserResponseById(UUID userId) {
        return userRepository.findById(userId)
                .map(u -> {
                            u.setProductList(u.getUserProductList()
                                    .stream()
                                    .map(p -> productFeign.getProduct(p.getProductId()))
                                    .toList());
                            return u;
                        })
                .map(MappingUtils::returnUserResponse)
                .orElseThrow(() -> new NoSuchElementException("No user found"));
    }

    private User getUserById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

}
