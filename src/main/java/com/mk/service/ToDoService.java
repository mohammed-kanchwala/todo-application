package com.mk.service;

import com.mk.exception.ServiceException;
import com.mk.model.ListDto;
import com.mk.model.ToDoDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ToDoService {

    List<ListDto> findAllList();

    List<ListDto> createList(Authentication authentication, String listName)
            throws ServiceException;

    List<ListDto> updateList(Authentication authentication, Long id, String listName);

    List<ListDto> deleteList(Authentication authentication, Long listId);

    List<ToDoDto> getAllTask(Long listId);

    List<ToDoDto> addTask(Long listId, ToDoDto todoTask) throws ServiceException;

    List<ToDoDto> updateTask(Long listId, Long taskId, ToDoDto todoTask) throws ServiceException;

    List<ToDoDto> deleteTask(Long listId, Long id);
}
