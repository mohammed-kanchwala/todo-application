package com.mk.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    @Email
    private String email;
    private String password;
    @Max(value = 100, message = "Name can not be more than 100 characters")
    private String firstName;
    @Max(value = 100, message = "Name can not be more than 100 characters")
    private String lastName;
    private Set<ListDto> lists;
}
