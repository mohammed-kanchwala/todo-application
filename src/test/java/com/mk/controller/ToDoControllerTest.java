package com.mk.controller;

import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.RoleDto;
import com.mk.util.RequestUtil;
import com.mk.util.TestUtility;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ToDoControllerTest extends UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ModelMapper mapper;

    private static Long roleId;

    @Test
    @Order(3)
    @DisplayName("Test Home Page")
    void get() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, "/"));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    @Order(4)
    @DisplayName("Get All List Empty Response")
    void getList() {
        URI uri =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<RoleDto> roleDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<RoleDto>>() {
        }.getType());
        assertEquals(0, roleDtoList.size());
    }

    @Test
    @Order(5)
    @DisplayName("Create List")
    void createList() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listName}");
        Map<String, Object> params = new HashMap<>();
        params.put("listName", "New List");
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<RoleDto> roleDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<RoleDto>>() {
        }.getType());
        assertEquals(1, roleDtoList.size());
        assertTrue(roleDtoList.stream().anyMatch(r -> r.getName().equalsIgnoreCase("New List")));
    }

    @Test
    @Order(6)
    @DisplayName("Get All List With Newly Created List")
    void getList_AfterCreation() {
        URI uri =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<RoleDto> roleDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<RoleDto>>() {}.getType());
        Optional<RoleDto> matchingRole =
                roleDtoList.stream().filter(r -> r.getName().equalsIgnoreCase(
                "New List")).findAny();

        assertTrue(matchingRole.isPresent());
        assertEquals(1, roleDtoList.size());
        matchingRole.ifPresent(r -> roleId = r.getId());
    }

    @Test
    void updateList() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listId}");
        Map<String, Object> params = new HashMap<>();
        params.put("listId", roleId);
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).queryParam("listName", "New Updated List").build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<RoleDto> roleDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<RoleDto>>() {}.getType());
        assertTrue(roleDtoList.stream().anyMatch(r -> r.getName().equalsIgnoreCase("New Updated List")));
    }

    @Test
    void deleteList() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listId}");
        Map<String, Object> params = new HashMap<>();
        params.put("listId", roleId);
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
                HttpMethod.DELETE, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<RoleDto> roleDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<RoleDto>>() {}.getType());
        assertFalse(roleDtoList.stream().anyMatch(r -> r.getName().equalsIgnoreCase("New Updated List")));
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