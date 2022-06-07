package com.mk.service;

import com.mk.exception.ServiceException;
import com.mk.model.RoleDto;
import com.mk.model.ToDoDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ToDoService {

  List<RoleDto> findAllList();

  List<RoleDto> createList(Authentication authentication, String listName)
    throws ServiceException;

  List<RoleDto> updateList(Authentication authentication, Long id, String listName);

  List<RoleDto> deleteList(Authentication authentication, Long listId);

  List<ToDoDto> addTask(Long listId, ToDoDto todoTask);

  List<ToDoDto> updateTask(Long listId, ToDoDto todoTask);

  List<ToDoDto> deleteTask(Long listId, Long id);
}
