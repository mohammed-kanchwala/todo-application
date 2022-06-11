package com.mk.util;

import com.mk.model.LoginUserDto;
import com.mk.model.ToDoDto;
import com.mk.model.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
	public static UserDto getRegisterUserDto(String email, String firstName,
					String lastName) {
		return UserDto.builder().email(email).password("password")
						.firstName(firstName).lastName(lastName).build();
	}

	public static LoginUserDto getLoginUserDto() {
		return LoginUserDto.builder().email("test@test.com").password("password")
						.build();
	}

	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(TestUtility.token);
		return headers;
	}

	public static ToDoDto createToDo(String title, boolean isDone) {
		ToDoDto todo = new ToDoDto();
		todo.setTitle(title);
		todo.setIsDone(isDone);
		todo.setCompletedDate(LocalDateTime.now());
		return todo;
	}
}
