package com.mk.controller;

import com.mk.constants.ErrorConstants;
import com.mk.constants.UrlConstants;
import com.mk.exception.ServiceException;
import com.mk.model.ApiResponse;
import com.mk.model.ToDoDto;
import com.mk.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = UrlConstants.API_URL + UrlConstants.TODO_URL)
@Validated
public class ToDoController {

  @Autowired
  private ToDoService toDoService;

  @GetMapping(value = "/")
  public ResponseEntity<ApiResponse<?>> get() {
    return ok(ApiResponse.builder().status(HttpStatus.OK).message("Welcome to the M.K's Simple To-Do Application APIs !!").build());
  }

  @GetMapping(value = UrlConstants.LIST)
  public ResponseEntity<ApiResponse<?>> getList() {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.findAllList()).build());
  }

  @PostMapping(value = UrlConstants.LIST + "/{listName}")
  public ResponseEntity<ApiResponse<?>> createList(@Size(max = 100, message =
          ErrorConstants.LINE_NAME_VALIDATION_MESSAGE) @PathVariable String listName, Authentication authentication) throws ServiceException {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.createList(authentication, listName)).message("TodoLists Created successfully !!").build());
  }

  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @PutMapping(value = UrlConstants.LIST + "/{listId}")
  public ResponseEntity<ApiResponse<?>> updateList(@PathVariable Long listId,
                                                   @Size(max = 100, message =
                                                           ErrorConstants.LINE_NAME_VALIDATION_MESSAGE) @RequestParam String listName, Authentication authentication) {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.updateList(authentication, listId, listName)).message("TodoLists Name updated successfully !!").build());
  }

  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @DeleteMapping(value = UrlConstants.LIST + "/{listId}")
  public ResponseEntity<ApiResponse<?>> deleteList(@PathVariable Long listId, Authentication authentication) {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.deleteList(authentication, listId)).message("TodoLists Deleted successfully !!").build());
  }


  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @GetMapping(value = UrlConstants.LIST + "/{listId}" + "/task")
  public ResponseEntity<ApiResponse<?>> getAllTask(@PathVariable Long listId, Authentication authentication) {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.getAllTask(listId)).build());
  }

  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @PostMapping(value = UrlConstants.LIST + "/{listId}" + "/task")
  public ResponseEntity<ApiResponse<?>> addTask(@PathVariable Long listId, @RequestBody ToDoDto todoTask, Authentication authentication) throws ServiceException {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.addTask(listId, todoTask)).message("Task added successfully !!").build());
  }

  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @PutMapping(value = UrlConstants.LIST + "/{listId}" + "/task" + "/{taskId}")
  public ResponseEntity<ApiResponse<?>> updateTask(@PathVariable Long listId, @PathVariable Long taskId, @Valid @RequestBody ToDoDto todoTask) throws ServiceException {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.updateTask(listId, taskId, todoTask)).message("Task updated successfully !!").build());
  }

  @PreAuthorize("@userSecurity.hasListAccess(authentication,#listId)")
  @DeleteMapping(value = UrlConstants.LIST + "/{listId}" + "/task" + "/{taskId}")
  public ResponseEntity<ApiResponse<?>> deleteTask(@PathVariable Long listId, @PathVariable Long taskId) {
    return ok(ApiResponse.builder().status(HttpStatus.OK).messages(toDoService.deleteTask(listId, taskId)).message("Task deleted successfully !!").build());
  }

}
