package com.mk.service.impl;

import com.mk.constants.ApplicationConstants;
import com.mk.entity.TodoLists;
import com.mk.entity.User;
import com.mk.exception.ServiceException;
import com.mk.model.ErrorInfo;
import com.mk.model.UserDto;
import com.mk.repository.ListRepository;
import com.mk.repository.UserRepository;
import com.mk.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ListRepository listRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public void register(UserDto user) throws Exception {

		User userEntity = userRepository.findByEmail(user.getEmail()).orElse(null);
		if (Objects.nonNull(userEntity)) {
			throw new ServiceException(ErrorInfo.builder()
							.code(HttpStatus.BAD_REQUEST.name())
							.message("Email id already registered !")
							.build());
		}
		userEntity = new User();
		BeanUtils.copyProperties(user, userEntity);
		userEntity.setPassword(encoder.encode(user.getPassword()));
		TodoLists todoLists = new TodoLists();
		todoLists.setName(ApplicationConstants.USER_ROLE);
		Set<TodoLists> todoListsSet = new HashSet<>();
		todoListsSet.add(todoLists);
		userEntity.setTodoLists(todoListsSet);
		userRepository.save(userEntity);
	}
}
