package com.mk.service;

import com.mk.model.UserDto;

public interface UserService {
	void register(UserDto user) throws Exception;
}
