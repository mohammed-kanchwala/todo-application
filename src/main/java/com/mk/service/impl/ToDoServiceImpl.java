package com.mk.service.impl;

import com.mk.constants.ApplicationConstants;
import com.mk.entity.List;
import com.mk.entity.ToDo;
import com.mk.entity.User;
import com.mk.exception.ServiceException;
import com.mk.model.ErrorInfo;
import com.mk.model.ListDto;
import com.mk.model.ToDoDto;
import com.mk.repository.ListRepository;
import com.mk.repository.ToDoRepository;
import com.mk.repository.UserRepository;
import com.mk.service.ToDoService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public java.util.List<ListDto> findAllList() {
        return getAllListNames();
    }

    private java.util.List<ListDto> getAllListNames() {
        java.util.List<List> list = StreamSupport.stream(
          listRepository.findAll().spliterator(), false).collect(Collectors.toList()).stream().filter(r -> !r.getName().equalsIgnoreCase(
          ApplicationConstants.USER_ROLE)).collect(Collectors.toList());
        return mapper.map(list, new TypeToken<java.util.List<ListDto>>() {}.getType());
    }

    @Override
    public java.util.List<ListDto> createList(Authentication authentication, String listName) throws ServiceException {
        List list = listRepository.findByName(listName).orElse(null);
        if (Objects.nonNull(list)) {
            throw new ServiceException(ErrorInfo.builder().code(HttpStatus.BAD_REQUEST.name()).message("A List with same name already exists").build());
        }
        List newList = new List();
        newList.setName(listName);
        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<List> lists = user.getLists();
            lists.add(newList);
            user.setLists(lists);
            userRepository.save(user);
            return getAllListNames();
        }
        throw new ServiceException(ErrorInfo.builder().code(HttpStatus.BAD_REQUEST.name()).message("Unable to create list").build());
    }

    @Override
    public java.util.List<ListDto> updateList(Authentication authentication, Long id, String listName) {
        Optional<List> optionalRole = listRepository.findByIdAndNameNot(id,
          ApplicationConstants.USER_ROLE);
        optionalRole.ifPresent(role -> {
            role.setName(listName);
            Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
            optionalUser.ifPresent(user -> {
                Set<List> lists = user.getLists();
                lists.add(role);
                user.setLists(lists);
                userRepository.save(user);
            });
        });
        return getAllListNames();
    }

    @Override
    public java.util.List<ListDto> deleteList(Authentication authentication, Long listId) {
        Optional<List> optionalRole = listRepository.findById(listId);
        optionalRole.ifPresent(role -> {
            Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
            optionalUser.ifPresent(user -> {
                Set<List> lists = user.getLists();
                lists.remove(role);
                user.setLists(lists);
                userRepository.save(user);
                listRepository.delete(role);
            });
        });
        return getAllListNames();
    }

    @Override
    public java.util.List<ToDoDto> getAllTask(Long listId) {
        return mapper.map(toDoRepository.findAllByListId(listId),
                new TypeToken<java.util.List<ToDoDto>>() {}.getType());
    }

    @Override
    public java.util.List<ToDoDto> addTask(Long listId, ToDoDto todoTask) throws ServiceException {
        ToDo todo = toDoRepository.findByTitle(todoTask.getTitle());
        if (Objects.nonNull(todo)) {
            throw new ServiceException(ErrorInfo.builder().code(HttpStatus.BAD_REQUEST.name()).message("A Task with same Title already exists").build());
        }
        ToDo task = new ToDo();
        BeanUtils.copyProperties(todoTask, task);
        List list = listRepository.findById(listId).orElse(null);
        if (Objects.nonNull(list)) {
            task.setList(list);
            toDoRepository.save(task);
        }
        return mapper.map(toDoRepository.findAllByListId(listId), new TypeToken<java.util.List<ToDoDto>>() {}.getType());
    }

    @Override
    public java.util.List<ToDoDto> updateTask(Long listId, Long taskId, ToDoDto todoTask) throws ServiceException {
        ToDo task = toDoRepository.findById(taskId).orElseThrow(() -> new ServiceException(ErrorInfo.builder().code(HttpStatus.BAD_REQUEST.name()).message("No task found to update !!").build()));
        task.setTitle(todoTask.getTitle());
        task.setCompletedDate(todoTask.getCompletedDate());
        task.setIsDone(todoTask.getIsDone());
        toDoRepository.save(task);
        return mapper.map(toDoRepository.findAllByListId(listId), new TypeToken<java.util.List<ToDoDto>>() {}.getType());
    }

    @Override
    public java.util.List<ToDoDto> deleteTask(Long listId, Long id) {
        toDoRepository.deleteById(id);
        return mapper.map(toDoRepository.findAllByListId(listId), new TypeToken<java.util.List<ToDoDto>>() {}.getType());
    }
}
