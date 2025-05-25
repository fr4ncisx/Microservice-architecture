package org.springcloud.users.domain.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Data
public class UserRequest {
    @NotNull @Email @NotBlank
    private String email;
    @NotNull @NotBlank @Length(min = 6, message = "La contraseña debe ser mínimo 6 caracteres")
    private String password;
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank
    private String lastName;
}
