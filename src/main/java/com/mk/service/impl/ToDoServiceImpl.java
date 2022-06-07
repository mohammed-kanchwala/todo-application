package com.mk.service.impl;

import com.mk.entity.Role;
import com.mk.entity.ToDo;
import com.mk.entity.User;
import com.mk.exception.ServiceException;
import com.mk.model.ErrorInfo;
import com.mk.model.RoleDto;
import com.mk.model.ToDoDto;
import com.mk.repository.RoleRepository;
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
    private RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAllList() {
        return getAllListNames();
    }

    private List<RoleDto> getAllListNames() {
        List<Role> list = StreamSupport.stream(roleRepository.findAll().spliterator(),
                        false).collect(Collectors.toList())
                .stream()
                .filter(r -> !r.getName().equalsIgnoreCase("USER"))
                .collect(Collectors.toList());
        return new ModelMapper().map(list, new TypeToken<List<RoleDto>>() {}.getType());
    }

    @Override
    public List<RoleDto> createList(Authentication authentication,
                                    String listName) throws ServiceException {
        Role role = roleRepository.findByName(listName).orElse(null);
        if (Objects.nonNull(role)) {
            throw new ServiceException(ErrorInfo.builder()
                    .code(HttpStatus.BAD_REQUEST.name())
              .message("A List with same name already exists")
              .build());
        }
        Role newRole = new Role();
        newRole.setName(listName);
        Optional<User> optionalUser = userRepository.findByEmail(
          authentication.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Role> roles = user.getRoles();
            roles.add(newRole);
            user.setRoles(roles);
            userRepository.save(user);
            return getAllListNames();
        }
        throw new ServiceException(ErrorInfo.builder()
          .code(HttpStatus.BAD_REQUEST.name())
          .message("Unable to create list")
          .build());
    }

    @Override
    public List<RoleDto> updateList(Authentication authentication,
                                    Long id,
                                    String listName) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        optionalRole.ifPresent(role -> {
            role.setName(listName);
            Optional<User> optionalUser = userRepository.findByEmail(
              authentication.getName());
            optionalUser.ifPresent(user -> {
                Set<Role> roles = user.getRoles();
                roles.add(role);
                user.setRoles(roles);
                userRepository.save(user);
            });
        });
        return getAllListNames();
    }

    @Override
    public List<RoleDto> deleteList(Authentication authentication, Long listId) {
        Optional<Role> optionalRole = roleRepository.findById(listId);
        optionalRole.ifPresent(role -> {
            Optional<User> optionalUser = userRepository.findByEmail(
              authentication.getName());
            optionalUser.ifPresent(user -> {
                Set<Role> roles = user.getRoles();
                roles.remove(role);
                user.setRoles(roles);
                userRepository.save(user);
                roleRepository.delete(role);
            });
        });
        return getAllListNames();
    }

    @Override
    public List<ToDoDto> addTask(Long listId, ToDoDto todoTask) {
        ToDo task = new ToDo();
        BeanUtils.copyProperties(todoTask, task);
        Role role = roleRepository.findById(listId).orElse(null);
        if (Objects.nonNull(role)) {
            task.setList(role);
            toDoRepository.save(task);
        }
        return toDoRepository.findAllByListId(listId);
    }

    @Override
    public List<ToDoDto> updateTask(Long listId, ToDoDto todoTask) {
        Optional<ToDo> task = toDoRepository.findById(todoTask.getId());
        task.ifPresent(t -> {
            t.setTitle(todoTask.getTitle());
            t.setCompletedDate(todoTask.getCompletedDate());
            t.setIsDone(todoTask.getIsDone());
            toDoRepository.save(t);
        });
        return toDoRepository.findAllByListId(listId);
    }

    @Override
    public List<ToDoDto> deleteTask(Long listId, Long id) {
        toDoRepository.deleteById(id);
        return toDoRepository.findAllByListId(listId);
    }
}
