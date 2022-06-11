package com.mk.controller;

import com.mk.constants.ErrorConstants;
import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.ErrorInfo;
import com.mk.model.ListDto;
import com.mk.model.ToDoDto;
import com.mk.util.MapperUtil;
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

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("rawtypes")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ToDoControllerTest extends UserControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private static Long listId;
  private static Long taskId;

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
  @Order(7)
  @DisplayName("Test Home Page")
  void homePage_WithRegisterAndLogin() {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TestUtility.createToDoURL(port, "/"));

    HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());
    ResponseEntity<ApiResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
  }

  @Test
  @Order(8)
  @DisplayName("Get All TodoLists Empty Response")
  void getList() {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.GET, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = MapperUtil.mapAll(apiResponse, ListDto.class);
    assertEquals(0, listDtoList.size());
  }

  @Test
  @Order(9)
  @DisplayName("Create Todo List")
  void createList() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listName}");
    Map<String, Object> params = new HashMap<>();
    params.put("listName", "New TodoLists");
    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .build()
            .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = MapperUtil.mapAll(apiResponse, ListDto.class);

    assertEquals(1, listDtoList.size());
  }


  @Test
  @Order(10)
  @DisplayName("Get All Todo List Including Newly Created")
  void getList_AfterCreation() {
    URI uri = UriComponentsBuilder.fromHttpUrl(
            TestUtility.createToDoURL(port, UrlConstants.LIST)).build().toUri();

    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());
    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.GET, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = new ModelMapper().map(apiResponse.getMessages(),
            new TypeToken<List<ListDto>>() {
            }.getType());
    Optional<ListDto> matchingRole = listDtoList.stream()
            .filter(r -> r.getName().equalsIgnoreCase("New TodoLists"))
            .findAny();

    assertTrue(matchingRole.isPresent());
    assertEquals(1, listDtoList.size());
    matchingRole.ifPresent(r -> listId = r.getId());
  }

  @Test
  @Order(11)
  @DisplayName("Update List Name")
  void updateList() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .queryParam("listName", "New Updated TodoLists")
            .build()
            .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.PUT, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = new ModelMapper().map(apiResponse.getMessages(),
            new TypeToken<List<ListDto>>() {
            }.getType());
    assertTrue(listDtoList.stream()
            .anyMatch(r -> r.getName().equalsIgnoreCase("New Updated TodoLists")));
  }

  @Test
  @Order(12)
  @DisplayName("Update List Name with Invalid Id")
  void updateList_WithInvalidId() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", 0);
    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .queryParam("listName", "New Updated TodoLists")
            .build()
            .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.PUT, request, ApiResponse.class);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = MapperUtil.map(apiResponse.getError(),
            ErrorInfo.class);
    assertEquals(ErrorConstants.ACCESS_DENIED, errorInfo.getCode());
    assertEquals(ErrorConstants.ACCESS_DENIED_MESSAGE, errorInfo.getMessage());
  }

  @Test
  @Order(13)
  @DisplayName("Get All the Tasks for an Empty List")
  void getAllTask() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}" + "/task");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .build()
            .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.GET, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ToDoDto> toDoDtoList = MapperUtil.mapAll(apiResponse, ToDoDto.class);
    assertEquals(0, toDoDtoList.size());
  }

  @Test
  @Order(14)
  @DisplayName("Add a new Task And Verify Updated List")
  void addTask() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}" + "/task");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);

    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .build()
            .toUri();
    ToDoDto toDoDto = RequestUtil.createToDo("New Task", true);
    HttpEntity<ToDoDto> request = new HttpEntity<>(toDoDto,
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.POST, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ToDoDto> toDoDtoList = new ModelMapper().map(apiResponse.getMessages(),
            new TypeToken<List<ToDoDto>>() {
            }.getType());
    assertEquals(1, toDoDtoList.size());
    Optional<ToDoDto> matchingDto =
            toDoDtoList.stream().filter(t -> t.getTitle().equals(toDoDto.getTitle())).findFirst();
    matchingDto.ifPresent(t -> taskId = t.getId());
  }

  @Test
  @Order(15)
  @DisplayName("Update Task with Valid Id")
  void updateTask() {
    ResponseEntity<ApiResponse> response = updateTaskTest(listId, taskId,
            RequestUtil.createToDo("Update Task", true));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ToDoDto> toDoDtoList = new ModelMapper().map(apiResponse.getMessages(),
            new TypeToken<List<ToDoDto>>() {
            }.getType());
    Optional<ToDoDto> optionalToDo =
            toDoDtoList.stream().filter(t -> t.getTitle().equals("Update Task")).findFirst();
    assertTrue(optionalToDo.isPresent());
  }


  @Test
  @Order(16)
  @DisplayName("Update Task with InValid Task Id")
  void updateTask_WithInvalidTaskId() {
    ResponseEntity<ApiResponse> response = updateTaskTest(listId, 0L,
            RequestUtil.createToDo("Update Task", true));

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = MapperUtil.map(apiResponse.getError(),
            ErrorInfo.class);
    assertEquals(HttpStatus.BAD_REQUEST.name(), errorInfo.getCode());
  }

  @Test
  @Order(17)
  @DisplayName("Update Task with InValid List Id")
  void updateTask_WithInvalidListId() {
    ResponseEntity<ApiResponse> response = updateTaskTest(0L, taskId,
            RequestUtil.createToDo("Update Task", true));

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = MapperUtil.map(apiResponse.getError(),
            ErrorInfo.class);
    assertEquals(ErrorConstants.ACCESS_DENIED, errorInfo.getCode());
    assertEquals(ErrorConstants.ACCESS_DENIED_MESSAGE, errorInfo.getMessage());
  }

  private ResponseEntity<ApiResponse> updateTaskTest(Long listId, Long taskId
          , ToDoDto task) {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}" + "/task" + "/{taskId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    params.put("taskId", taskId);

    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .build()
            .toUri();
    HttpEntity<ToDoDto> request = new HttpEntity<>(task, RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.PUT, request, ApiResponse.class);
    return response;
  }


  @Test
  @Order(18)
  @DisplayName("Delete Task")
  void deleteTask() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}" + "/task" + "/{taskId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    params.put("taskId", taskId);

    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params)
            .build()
            .toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.DELETE, request, ApiResponse.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ToDoDto> toDoDtoList = new ModelMapper().map(apiResponse.getMessages(),
            new TypeToken<List<ToDoDto>>() {
            }.getType());
    Optional<ToDoDto> optionalToDo =
            toDoDtoList.stream().filter(t -> t.getId().equals(taskId)).findFirst();
    assertFalse(optionalToDo.isPresent());
  }

  @Test
  @Order(19)
  @DisplayName("Delete Todo List")
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
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getMessages());
    List<ListDto> listDtoList = MapperUtil.mapAll(apiResponse, ListDto.class);
    assertFalse(listDtoList.stream()
            .anyMatch(r -> r.getName().equalsIgnoreCase("New Updated TodoLists")));
  }

  @Test
  @Order(20)
  @DisplayName("Delete TodoLists Failure")
  void afterDeleteTest() {
    String url = TestUtility.createToDoURL(port,
            UrlConstants.LIST + "/{listId}");
    Map<String, Object> params = new HashMap<>();
    params.put("listId", listId);
    URI uri = UriComponentsBuilder.fromUriString(url)
            .uriVariables(params).build().toUri();
    HttpEntity<HttpHeaders> request = new HttpEntity<>(
            RequestUtil.getHeaders());

    ResponseEntity<ApiResponse> response = restTemplate.exchange(uri,
            HttpMethod.DELETE, request, ApiResponse.class);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    assertNotNull(response.getBody());
    ApiResponse apiResponse = MapperUtil.map(response.getBody(),
            ApiResponse.class);
    assertNotNull(apiResponse.getError());
    ErrorInfo errorInfo = MapperUtil.map(apiResponse.getError(),
            ErrorInfo.class);
    assertEquals(ErrorConstants.ACCESS_DENIED, errorInfo.getCode());
    assertEquals(ErrorConstants.ACCESS_DENIED_MESSAGE, errorInfo.getMessage());
  }
}
