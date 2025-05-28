package org.springcloud.users.utils;

import java.util.Optional;
import java.util.function.Supplier;

public final class ValidationUtils {

    private ValidationUtils(){}

    public static <T> T assertUserNotNull(T object, Supplier<? extends RuntimeException> exceptionSupplier) {
        return Optional.ofNullable(object)
                .orElseThrow(exceptionSupplier);
    }
}
