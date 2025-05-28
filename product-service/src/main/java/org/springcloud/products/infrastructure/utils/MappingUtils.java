package org.springcloud.products.infrastructure.utils;

import org.modelmapper.ModelMapper;

public final class MappingUtils {

    private MappingUtils(){}

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T, D> T mapToClass(D source, Class<T> clazz) {
        return modelMapper.map(source, clazz);
    }

    public static <T, D> void mapToExistingInstance(D source, T destination) {
        modelMapper.map(source, destination);
    }

}
