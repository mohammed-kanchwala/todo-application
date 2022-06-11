package com.mk.util;

import com.mk.model.ApiResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperUtil {

	private static ModelMapper modelMapper;

	static {
		modelMapper = new ModelMapper();
	}

	public static <D, T> D map(final T entity, Class<D> outClass) {
		return modelMapper.map(entity, outClass);
	}

	public static <D, T> List<D> mapAll(final Collection<T> entityList,
					Class<D> outCLass) {
		return entityList.stream()
						.map(entity -> map(entity, outCLass))
						.collect(Collectors.toList());
	}

	public static <D> List<D> mapAll(ApiResponse apiResponse, Class<D> outClass) {
		return new ModelMapper().map(apiResponse.getMessages(),
						new TypeToken<List<D>>() {
						}.getType());
	}
}
