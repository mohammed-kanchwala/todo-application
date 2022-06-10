package com.mk.service.impl;

import com.mk.constants.ApplicationConstants;
import com.mk.entity.ToDo;
import com.mk.entity.TodoLists;
import com.mk.entity.User;
import com.mk.exception.ServiceException;
import com.mk.model.ErrorInfo;
import com.mk.model.ListDto;
import com.mk.model.ToDoDto;
import com.mk.repository.ListRepository;
import com.mk.repository.ToDoRepository;
import com.mk.repository.UserRepository;
import com.mk.service.ToDoService;
import com.mk.util.MapperUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
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

  @Override
  public List<ListDto> findAllList() {
    return getAllListNames();
  }

  private List<ListDto> getAllListNames() {
    List<TodoLists> todoLists = StreamSupport.stream(
        listRepository.findAll().spliterator(), false)
      .collect(Collectors.toList())
      .stream()
      .filter(
        r -> !r.getName().equalsIgnoreCase(ApplicationConstants.USER_ROLE))
      .collect(Collectors.toList());
    return MapperUtil.mapAll(todoLists, ListDto.class);
  }

  @Override
  public List<ListDto> createList(Authentication authentication,
    String listName) throws ServiceException {
    TodoLists todoLists = listRepository.findByName(listName).orElse(null);
    if (Objects.nonNull(todoLists)) {
      throw new ServiceException(ErrorInfo.builder()
        .code(HttpStatus.BAD_REQUEST.name())
        .message("A TodoLists with same name already exists")
        .build());
    }
    TodoLists newTodoLists = new TodoLists();
    newTodoLists.setName(listName);
    Optional<User> optionalUser = userRepository.findByEmail(
      authentication.getName());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      Set<TodoLists> listsSet = user.getTodoLists();
      listsSet.add(newTodoLists);
      user.setTodoLists(listsSet);
      userRepository.save(user);
      return getAllListNames();
    }
    throw new ServiceException(ErrorInfo.builder()
      .code(HttpStatus.BAD_REQUEST.name())
      .message("Unable to create todoLists")
      .build());
  }

  @Override
  public List<ListDto> updateList(Authentication authentication,
    Long id,
    String listName) {
    Optional<TodoLists> optionalRole = listRepository.findByIdAndNameNot(id,
      ApplicationConstants.USER_ROLE);
    optionalRole.ifPresent(role -> {
      role.setName(listName);
      Optional<User> optionalUser = userRepository.findByEmail(
        authentication.getName());
      optionalUser.ifPresent(user -> {
        Set<TodoLists> todoLists = user.getTodoLists();
        todoLists.add(role);
        user.setTodoLists(todoLists);
        userRepository.save(user);
      });
    });
    return getAllListNames();
  }

  @Override
  public List<ListDto> deleteList(Authentication authentication, Long listId) {
    Optional<TodoLists> optionalRole = listRepository.findById(listId);
    optionalRole.ifPresent(role -> {
      Optional<User> optionalUser = userRepository.findByEmail(
        authentication.getName());
      optionalUser.ifPresent(user -> {
        Set<TodoLists> todoLists = user.getTodoLists();
        todoLists.remove(role);
        user.setTodoLists(todoLists);
        userRepository.save(user);
        listRepository.delete(role);
      });
    });
    return getAllListNames();
  }

  @Override
  public List<ToDoDto> getAllTask(Long listId) {
    return MapperUtil.mapAll(toDoRepository.findAllByListId(listId),
      ToDoDto.class);
  }

  @Override
  public List<ToDoDto> addTask(Long listId, ToDoDto todoTask)
    throws ServiceException {
    ToDo todo = toDoRepository.findByTitle(todoTask.getTitle());
    if (Objects.isNull(todo)) {
      todo = new ToDo();
    }
    BeanUtils.copyProperties(todoTask, todo);
    TodoLists todoLists = listRepository.findById(listId).orElse(null);
    if (Objects.nonNull(todoLists)) {
      todo.setList(todoLists);
      toDoRepository.save(todo);
    }
    return MapperUtil.mapAll(toDoRepository.findAllByListId(listId),
      ToDoDto.class);
  }

  @Override
  public List<ToDoDto> updateTask(Long listId, Long taskId, ToDoDto todoTask)
    throws ServiceException {
    ToDo task = toDoRepository.findById(taskId)
      .orElseThrow(() -> new ServiceException(ErrorInfo.builder()
        .code(HttpStatus.BAD_REQUEST.name())
        .message("No task found to update !!")
        .build()));
    task.setTitle(todoTask.getTitle());
    task.setCompletedDate(todoTask.getCompletedDate());
    task.setIsDone(todoTask.getIsDone());
    toDoRepository.save(task);
    return MapperUtil.mapAll(toDoRepository.findAllByListId(listId),
      ToDoDto.class);
  }

  @Override
  public List<ToDoDto> deleteTask(Long listId, Long id) {
    toDoRepository.deleteById(id);
    return MapperUtil.mapAll(toDoRepository.findAllByListId(listId),
      ToDoDto.class);
  }
}
