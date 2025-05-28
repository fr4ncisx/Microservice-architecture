package org.springcloud.products.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {
    @NotBlank @NotNull
    private String name;
    @NotBlank @NotNull
    private String description;
    @NotNull @Positive
    private Long price;
}
