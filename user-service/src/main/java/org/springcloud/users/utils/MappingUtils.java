package org.springcloud.users.utils;

import org.modelmapper.ModelMapper;
import org.springcloud.users.domain.models.dto.response.UserResponse;
import org.springcloud.users.domain.models.entity.User;

public final class MappingUtils {

    private MappingUtils(){}

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T, D> T mapToClass(D source, Class<T> clazz) {
        return modelMapper.map(source, clazz);
    }

    public static <T, D> void mapToExistingInstance(D source, T destination) {
        modelMapper.map(source, destination);
    }

    public static UserResponse returnUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
