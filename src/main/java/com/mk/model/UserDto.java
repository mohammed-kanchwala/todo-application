package com.mk.model;

import com.mk.entity.RoleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserDto implements Serializable {
    private final Long id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Set<RoleDto> roles;
}
