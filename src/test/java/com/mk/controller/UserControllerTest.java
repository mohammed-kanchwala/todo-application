package com.mk.controller;

import com.mk.constants.ErrorConstants;
import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.JwtResponse;
import com.mk.model.LoginUserDto;
import com.mk.model.UserDto;
import com.mk.util.RequestUtil;
import com.mk.util.TestUtility;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private static String TOKEN;

  @Autowired
  private ModelMapper mapper;

  @Test
  @Order(2)
  @DisplayName("User Registration Test For Wrong input")
  void registerUser_WithWrongInput() {
    URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.REGISTER)).build().toUri();

    UserDto userDto = RequestUtil.getRegisterUserDto("test",
            "FirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstNameFirstName", "LastNameLastNameLastNameLastNameLastNameLastNameLastNameLastNameLastNameLastNameLastNameLastNameLastName");
    HttpEntity<UserDto> request = new HttpEntity<>(userDto);
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(3, response.getBody().getFieldErrors().size());
  }

  @Test
  @Order(3)
  @DisplayName("User Registration Test")
  void registerUser() {
    URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.REGISTER)).build().toUri();

    UserDto userDto = RequestUtil.getRegisterUserDto("test@test.com",
            "First", "Last");
    HttpEntity<UserDto> request = new HttpEntity<>(userDto);
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.OK,
            Objects.requireNonNull(response.getBody()).getStatus());
  }

  @Test
  @Order(4)
  @DisplayName("User Authentication Test With Wrong Input")
  void authenticateUser_WithWrongInput() {
    URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.AUTHENTICATE)).build().toUri();

    UserDto userDto =
            UserDto.builder().email("test").password("password").build();
    HttpEntity<UserDto> request = new HttpEntity<>(userDto);
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  @Order(5)
  @DisplayName("User Authentication Test With Invalid Password")
  void authenticateUser_WithInvalidPassword() {
    URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.AUTHENTICATE)).build().toUri();

    UserDto userDto =
            UserDto.builder().email("test@test.com").password("wrongpassword").build();
    HttpEntity<UserDto> request = new HttpEntity<>(userDto);
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(ErrorConstants.INVALID_CREDENTIALS,
            response.getBody().getError().getCode());
  }

  @Test
  @Order(6)
  @DisplayName("User Authentication Test")
  void authenticateUser() {
    URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.AUTHENTICATE)).build().toUri();

    LoginUserDto userDto = RequestUtil.getLoginUserDto();
    HttpEntity<LoginUserDto> request = new HttpEntity<>(userDto);
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.OK,
            Objects.requireNonNull(response.getBody()).getStatus());
    assertNotNull(response.getBody().getMessage());
    JwtResponse jwtResponse = mapper.map(response.getBody().getMessage(), JwtResponse.class);
    assertNotNull(jwtResponse.getJwtToken());
    TestUtility.token = jwtResponse.getJwtToken();
  }
}
