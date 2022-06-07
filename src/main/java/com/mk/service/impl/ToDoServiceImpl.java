package com.mk.service.impl;

import com.mk.entity.Role;
import com.mk.entity.ToDo;
import com.mk.entity.User;
import com.mk.model.ToDoDto;
import com.mk.repository.RoleRepository;
import com.mk.repository.ToDoRepository;
import com.mk.repository.UserRepository;
import com.mk.service.ToDoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<String> findAllList() {
        return StreamSupport.stream(roleRepository.findAll().spliterator(),
          false).map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public String createList(Authentication authentication, String listName) {
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
            return "List : '" + listName + "' created successfully.";
        }
        return "Unable to create list";
    }

    @Override
    public String updateList(Authentication authentication,
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
        return "List : '" + listName + "' updated " + "successfully.";
    }

    @Override
    public String deleteList(Authentication authentication, Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
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
        return "List deleted successfully.";
    }

    @Override
    public List<ToDoDto> addTask(String listName, ToDoDto todoTask) {
        ToDo task = new ToDo();
        BeanUtils.copyProperties(todoTask, task);
        Role role = roleRepository.findByName(listName).orElse(null);
        if (Objects.nonNull(role)) {
            task.setListName(role);
            toDoRepository.save(task);
        }
        return toDoRepository.findAllByListName(listName);
    }

    @Override
    public List<ToDoDto> updateTask(String listName, ToDoDto todoTask) {
        Optional<ToDo> task = toDoRepository.findById(todoTask.getId());
        task.ifPresent(t -> {
            t.setTitle(todoTask.getTitle());
            t.setCompletedDate(todoTask.getCompletedDate());
            t.setIsDone(todoTask.getIsDone());
            toDoRepository.save(t);
        });
        return toDoRepository.findAllByListName(listName);
    }

    @Override
    public List<ToDoDto> deleteTask(String listName, Long id) {
        toDoRepository.deleteById(id);
        return toDoRepository.findAllByListName(listName);
    }
}
