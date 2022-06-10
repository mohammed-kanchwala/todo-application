package com.mk.controller;

import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.JwtResponse;
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
    @DisplayName("User Registration Test")
    void registerUser() {
        URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.REGISTER)).build().toUri();

        UserDto userDto = RequestUtil.getRegisterUserDto();
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK,
          Objects.requireNonNull(response.getBody()).getStatus());
    }


    @Test
    @Order(3)
    @DisplayName("User Authentication Test")
    void authenticateUser() {
        URI uri = UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port, UrlConstants.AUTHENTICATE)).build().toUri();

        UserDto userDto = RequestUtil.getLoginUserDto();
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
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
