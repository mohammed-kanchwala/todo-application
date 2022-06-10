package com.mk.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<ListDto> lists;
}
