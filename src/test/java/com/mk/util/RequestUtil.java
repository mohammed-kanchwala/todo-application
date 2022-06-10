package com.mk.util;

import com.mk.model.ToDoDto;
import com.mk.model.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
    public static UserDto getRegisterUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");
        userDto.setFirstName("First");
        userDto.setLastName("Last");
        return userDto;
    }

    public static UserDto getLoginUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");
        return userDto;
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
