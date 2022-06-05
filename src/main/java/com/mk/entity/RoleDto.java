package com.mk.entity;

import com.mk.model.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class RoleDto implements Serializable {
    private final Long id;
    private final String name;
    private Set<UserDto> userDto;
}
