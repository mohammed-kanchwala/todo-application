package com.mk.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class UserDto implements Serializable {
	private Long id;
	@Email
	@NotBlank
	private String email;
	private String password;
	@Size(max = 100, message = "Name can not be more than 100 characters")
	@NotBlank
	private String firstName;
	@Size(max = 100, message = "Name can not be more than 100 characters")
	@NotBlank
	private String lastName;
	private Set<ListDto> lists;
}
