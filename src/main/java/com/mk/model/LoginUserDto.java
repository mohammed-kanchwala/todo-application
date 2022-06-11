package com.mk.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
public class LoginUserDto implements Serializable {
	private Long id;
	@Email
	@NotBlank
	private String email;
	private String password;
}
