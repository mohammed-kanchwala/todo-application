package com.mk.controller;

import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.JwtResponse;
import com.mk.model.UserDto;
import com.mk.util.RequestUtil;
import com.mk.util.TestUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static String TOKEN;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("User Registration Test")
    void registerUser() {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port,
                        UrlConstants.REGISTER));

        UserDto userDto = RequestUtil.getRegisterUserDto();
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(builder.toUriString(),
                        HttpMethod.POST,
                        request,
                        ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }


    @Test
    @DisplayName("User Authentication Test")
    void authenticateUser() {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createUserURL(port,
                        UrlConstants.AUTHENTICATE));

        UserDto userDto = RequestUtil.getLoginUserDto();
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(builder.toUriString(),
                        HttpMethod.POST,
                        request,
                        ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
        assertNotNull(response.getBody().getMessage());
        JwtResponse jwtResponse = mapper.map(response.getBody().getMessage(),
                JwtResponse.class);
        assertNotNull(jwtResponse.getJwtToken());
        TestUtility.token = jwtResponse.getJwtToken();
    }
}