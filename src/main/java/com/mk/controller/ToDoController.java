package com.mk.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = UrlConstants.API_URL + UrlConstants.TODO_URL)
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping(value = "/")
    public ResponseEntity<ApiResponse<?>> get() {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .message("Welcome to the M.K's Simple To-Do Application !!")
          .build());
    }

    @GetMapping(value = UrlConstants.LIST)
    public ResponseEntity<ApiResponse<?>> getList() {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .message(toDoService.findAllList())
          .build());
    }

    @PostMapping(value = UrlConstants.LIST + "/{listName}")
    public ResponseEntity<ApiResponse<?>> createList(@PathVariable(
      "listName"
    ) String listName, Authentication authentication) throws ServiceException {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.createList(authentication, listName))
          .message("List Created successfully !!")
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PutMapping(value = UrlConstants.LIST + "/{id}")
    public ResponseEntity<ApiResponse<?>> updateList(@PathVariable(
      "id"
    ) Long id,
      @RequestParam("listName") String listName,
      Authentication authentication) {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.updateList(authentication, id, listName))
          .message("List Name updated successfully !!")
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @DeleteMapping(value = UrlConstants.LIST + "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteList(@PathVariable(
      "id"
    ) Long id, Authentication authentication) {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.deleteList(authentication, id))
          .message("List Deleted successfully !!")
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PostMapping(value = UrlConstants.LIST + "/{listName}" + "/task")
    public ResponseEntity<ApiResponse<?>> addTask(@PathVariable(
      "listName"
    ) String listName,
      @RequestBody ToDoDto todoTask,
      Authentication authentication) {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.addTask(listName, todoTask))
          .message("Task added successfully !!")
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PutMapping(value = UrlConstants.LIST + "/{listName}" + "/task")
    public ResponseEntity<ApiResponse<?>> updateTask(@PathVariable(
      "listName"
    ) String listName, @RequestBody ToDoDto todoTask) {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.updateTask(listName, todoTask))
          .message("Task updated successfully !!")
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @DeleteMapping(
      value = UrlConstants.LIST + "/{listName}" + "/task" + "/{id}"
    )
    public ResponseEntity<ApiResponse<?>> deleteTask(@PathVariable(
      "listName"
    ) String listName, @PathVariable("id") Long id) {
        return ok(ApiResponse.builder()
          .status(HttpStatus.OK)
          .messages(toDoService.deleteTask(listName, id))
          .message("Task deleted successfully !!")
          .build());
    }

}
