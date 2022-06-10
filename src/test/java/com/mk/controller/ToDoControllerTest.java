package com.mk.controller;

import com.mk.constants.ErrorConstants;
import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.ErrorInfo;
import com.mk.model.ListDto;
import com.mk.util.RequestUtil;
import com.mk.util.TestUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ToDoControllerTest extends UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ModelMapper mapper;

    private static Long listId;

    @Test
    @Order(1)
    @DisplayName("Test Home Page Without Register and Login")
    void homePage_WithoutRegisterAndLogin() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, "/"));

        ResponseEntity<Object> response =
          restTemplate.getForEntity(builder.toUriString(), Object.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Test Home Page")
    void homePage_WithRegisterAndLogin() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, "/"));

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    @Order(5)
    @DisplayName("Get All TodoLists Empty Response")
    void getList() {
        URI uri =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<ListDto> listDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<ListDto>>() {
        }.getType());
        assertEquals(0, listDtoList.size());
    }

    @Test
    @Order(5)
    @DisplayName("Create TodoLists")
    void createList() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listName}");
        Map<String, Object> params = new HashMap<>();
        params.put("listName", "New TodoLists");
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<ListDto> listDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<ListDto>>() {
        }.getType());
        assertEquals(1, listDtoList.size());
        assertTrue(listDtoList.stream().anyMatch(r -> r.getName().equalsIgnoreCase("New TodoLists")));
    }

    @Test
    @Order(6)
    @DisplayName("Get All TodoLists With Newly Created TodoLists")
    void getList_AfterCreation() {
        URI uri =
                UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, ApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
        assertNotNull(apiResponse.getMessages());
        List<ListDto> listDtoList = mapper.map(apiResponse.getMessages(), new TypeToken<List<ListDto>>() {}.getType());
        Optional<ListDto> matchingRole =
                listDtoList.stream().filter(r -> r.getName().equalsIgnoreCase(
                "New TodoLists")).findAny();

        assertTrue(matchingRole.isPresent());
        assertEquals(1, listDtoList.size());
        matchingRole.ifPresent(r -> listId = r.getId());
    }

  @Test
  @Order(7)
  @DisplayName("Update TodoLists Name")
    void updateList() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listId}");
        Map<String, Object> params = new HashMap<>();
        params.put("listId", listId);
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).queryParam("listName", "New Updated TodoLists").build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = mapper.map(apiResponse.getMessages(),
      new TypeToken<List<ListDto>>() {

      }.getType());
    assertTrue(listDtoList.stream()
      .anyMatch(r -> r.getName().equalsIgnoreCase("New Updated TodoLists")));
  }

  @Test
  @Order(8)
  @DisplayName("Update TodoLists Name with Invalid Id")
    void updateList_WithInvalidId() {
        String url = TestUtility.createToDoURL(port, UrlConstants.LIST + "/{listId}");
        Map<String, Object> params = new HashMap<>();
        params.put("listId", 0);
        URI uri = UriComponentsBuilder.fromUriString(url).uriVariables(params).queryParam("listName", "New Updated TodoLists").build().toUri();
        HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(uri, HttpMethod.PUT, request, ApiResponse.class);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = mapper.map(apiResponse.getError(), ErrorInfo.class);
    assertEquals(ErrorConstants.ACCESS_DENIED, errorInfo.getCode());
    assertEquals(ErrorConstants.ACCESS_DENIED_MESSAGE, errorInfo.getMessage());
  }

  @Test
  void getAllTask() {
    String url = TestUtility.createToDoURL(port, UrlConstants.LIST +
      "/{listId}" + "/task");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri =
      UriComponentsBuilder.fromUriString(url).uriVariables(params).build().toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
      HttpMethod.GET, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
    assertNotNull(apiResponse.getMessages());

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

  @Test
  @Order(12)
  @DisplayName("Delete TodoLists")
  void deleteList() {
    String url = TestUtility.createToDoURL(port,
      UrlConstants.LIST + "/{listId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri = UriComponentsBuilder.fromUriString(url)
      .uriVariables(params)
      .build()
      .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
      RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
      HttpMethod.DELETE, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = mapper.map(apiResponse.getMessages(),
      new TypeToken<List<ListDto>>() {

      }.getType());
    assertFalse(listDtoList.stream()
      .anyMatch(r -> r.getName().equalsIgnoreCase("New Updated TodoLists")));
  }

  @Test
  @Order(13)
  @DisplayName("Delete TodoLists Failure")
  void afterDeleteTest() {
    String url = TestUtility.createToDoURL(port,
      UrlConstants.LIST + "/{listId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri = UriComponentsBuilder.fromUriString(url)
      .uriVariables(params)
      .build()
      .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
      RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
      HttpMethod.DELETE, request, ApiResponse.class);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = mapper.map(response.getBody(), ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = mapper.map(apiResponse.getError(), ErrorInfo.class);
    assertEquals(ErrorConstants.ACCESS_DENIED, errorInfo.getCode());
    assertEquals(ErrorConstants.ACCESS_DENIED_MESSAGE, errorInfo.getMessage());
  }
}
