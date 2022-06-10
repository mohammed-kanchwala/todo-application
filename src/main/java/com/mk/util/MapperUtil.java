package com.mk.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperUtil {

  @Autowired
  private static ModelMapper modelMapper;

  public static <D, T> D map(final T entity, Class<D> outClass) {
    return modelMapper.map(entity, outClass);
  }

  public static <D, T> List<D> mapAll(final Collection<T> entityList,
    Class<D> outCLass) {
    return entityList.stream()
      .map(entity -> map(entity, outCLass))
      .collect(Collectors.toList());
  }

}
