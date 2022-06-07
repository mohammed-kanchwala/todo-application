package com.mk.controller;

import com.mk.constants.UrlConstants;
import com.mk.model.ApiResponse;
import com.mk.model.ToDoDto;
import com.mk.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = UrlConstants.API_URL + UrlConstants.TODO_URL)
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping(value = "/")
    public ResponseEntity<?> get() {
        return ok(ApiResponse.builder()
          .message("Welcome to the M.K's Simple To-Do Application !!")
          .build());

    }

    @GetMapping(value = UrlConstants.LIST)
    public ResponseEntity<?> getList(@AuthenticationPrincipal Principal principal) {
        return ok(
          ApiResponse.builder().message(toDoService.findAllList()).build());
    }

    @PostMapping(value = UrlConstants.LIST + "/{listName}")
    public ResponseEntity<?> createList(@PathVariable(
      "listName"
    ) String listName, Authentication authentication) {
        return ok(ApiResponse.builder()
          .message(toDoService.createList(authentication, listName))
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PutMapping(value = UrlConstants.LIST + "/{id}")
    public ResponseEntity<?> updateList(@PathVariable("id") Long id,
      @RequestParam("listName") String listName,
      Authentication authentication) {
        return ok(ApiResponse.builder()
          .message(toDoService.updateList(authentication, id, listName))
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @DeleteMapping(value = UrlConstants.LIST + "/{id}")
    public ResponseEntity<?> updateList(@PathVariable("id") Long id,
      Authentication authentication) {
        return ok(ApiResponse.builder()
          .message(toDoService.deleteList(authentication, id))
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PostMapping(value = UrlConstants.LIST + "/{listName}" + "/task")
    public ResponseEntity<?> addTask(@PathVariable("listName") String listName,
      @RequestBody ToDoDto todoTask,
      Authentication authentication) {
        return ok(ApiResponse.builder()
          .message(toDoService.addTask(listName, todoTask))
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PutMapping(value = UrlConstants.LIST + "/{listName}" + "/task")
    public ResponseEntity<?> updateTask(@PathVariable("listName") String listName,
      @RequestBody ToDoDto todoTask) {
        return ok(ApiResponse.builder()
          .message(toDoService.updateTask(listName, todoTask))
          .build());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @DeleteMapping(value = UrlConstants.LIST + "/{listName}" + "/task" +
      "/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("listName") String listName, @PathVariable("id") Long id) {
        return ok(ApiResponse.builder()
          .message(toDoService.deleteTask(listName, id))
          .build());
    }

}
