package com.mk.controller;

import com.mk.constants.UrlConstants;
import com.mk.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = UrlConstants.API_URL + UrlConstants.TODO_URL)
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping(value = "/")
    public ResponseEntity<?> get() {
        return ok("Welcome to the M.K's Simple To-Do Application !!");

    }

    @GetMapping(value = UrlConstants.LIST)
    public ResponseEntity<?> getList(@AuthenticationPrincipal Principal principal) {
        return ok(toDoService.findAllList());
    }

    @PreAuthorize("@userSecurity.hasListAccess(authentication,#listName)")
    @PostMapping(value = UrlConstants.LIST + "/{listName}")
    public ResponseEntity<?> getList(@PathVariable("listName") String listName, Authentication authentication) {
        return ok(toDoService.findAllList());
    }
}
