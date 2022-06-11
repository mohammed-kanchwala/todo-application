package com.mk.controller;

import com.mk.constants.ErrorConstants;
import com.mk.exception.ServiceException;
import com.mk.model.*;
import com.mk.service.UserService;
import com.mk.service.impl.JwtUserDetailsService;
import com.mk.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.mk.constants.UrlConstants.*;

@RestController
@RequestMapping(value = API_URL + USER_URL)
@Validated
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private JwtUserDetailsService userDetailsService;

  @PostMapping(value = REGISTER)
  public ResponseEntity<ApiResponse<?>> registerUser(@Validated @RequestBody UserDto user)
          throws Exception {
    userService.register(user);
    return ResponseEntity.ok(ApiResponse.builder()
            .status(HttpStatus.OK)
            .message("Registration Successful !!")
            .build());
  }

  @PostMapping(value = AUTHENTICATE)
  public ResponseEntity<ApiResponse<?>> authenticateUser(@Validated @RequestBody LoginUserDto authenticationRequest)
          throws Exception {

    authenticate(authenticationRequest.getEmail(),
            authenticationRequest.getPassword());

    final UserDetails userDetails = userDetailsService.loadUserByUsername(
            authenticationRequest.getEmail());

    return ResponseEntity.ok(ApiResponse.builder()
            .status(HttpStatus.OK)
            .message(new JwtResponse(jwtTokenUtil.generateToken(userDetails)))
            .build());
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password));
    } catch (BadCredentialsException e) {
      throw new ServiceException(ErrorInfo.builder()
              .code(ErrorConstants.INVALID_CREDENTIALS)
              .message("Invalid Credentials")
              .build());
    }
  }
}
