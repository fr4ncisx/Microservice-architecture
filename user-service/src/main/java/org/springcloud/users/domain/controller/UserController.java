package org.springcloud.users.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springcloud.users.domain.models.dto.request.UserRequest;
import org.springcloud.users.domain.models.dto.response.UserResponse;
import org.springcloud.users.domain.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest){
        return ResponseEntity.ok(userService.create(userRequest));
    }

    @PutMapping("{userId}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserRequest userRequest){
        return ResponseEntity.ok(userService.update(userId, userRequest));

    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.delete(userId));
    }

    @PostMapping("/assign")
    public ResponseEntity<UserResponse> assignProduct(@RequestParam UUID userId,@RequestParam UUID productId){
        return ResponseEntity.ok(userService.addProductToUser(productId, userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProductOfUser(@RequestParam UUID productId, @RequestParam UUID userId){
        userService.deleteProductFromUser(productId, userId);
        return ResponseEntity.noContent().build();
    }

}
