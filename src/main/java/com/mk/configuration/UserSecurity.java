package com.mk.configuration;

import com.mk.constants.ApplicationConstants;
import com.mk.entity.TodoLists;
import com.mk.entity.User;
import com.mk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    UserRepository userRepository;

    public boolean hasListAccess(Authentication authentication, String listName) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            Set<TodoLists> todoLists = null;
            todoLists = user.get().getTodoLists();
            List<String> list = todoLists.stream().map(TodoLists::getName).collect(Collectors.toList());
            list.removeIf(s -> s.equalsIgnoreCase(ApplicationConstants.USER_ROLE));
            return list.contains(listName);
        }
        return false;
    }

    public boolean hasListAccess(Authentication authentication, Long listId) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            Set<TodoLists> todoLists = user.get().getTodoLists();
            todoLists.removeIf(l -> l.getName().equalsIgnoreCase(ApplicationConstants.USER_ROLE));
            List<Long> list = todoLists.stream().map(TodoLists::getId).collect(Collectors.toList());
            return list.contains(listId);
        }
        return false;
    }
}

