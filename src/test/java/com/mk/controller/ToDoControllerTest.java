package com.mk.controller;

import com.mk.util.TestUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToDoControllerTest extends UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test Application Context")
    void get() {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, "/"));
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void getList() {
    }

    @Test
    void createList() {
    }

    @Test
    void updateList() {
    }

    @Test
    void deleteList() {
    }

    @Test
    void getAllTask() {
    }

    @Test
    void addTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}