package com.mk.service;

import com.mk.exception.ServiceException;
import com.mk.model.ToDoDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ToDoService {

  List<String> findAllList();

  List<String> createList(Authentication authentication, String listName)
    throws ServiceException;

  List<String> updateList(Authentication authentication, Long id, String listName);

  List<String> deleteList(Authentication authentication, Long listName);

  List<ToDoDto> addTask(String listName, ToDoDto todoTask);

  List<ToDoDto> updateTask(String listName, ToDoDto todoTask);

  List<ToDoDto> deleteTask(String listName, Long id);
}
