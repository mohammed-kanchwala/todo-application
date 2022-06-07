package com.mk.configuration;

import com.mk.entity.Role;
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
            Set<Role> roles = null;
            roles = user.get().getRoles();
            List<String> list = roles.stream().map(Role::getName).collect(Collectors.toList());
            return list.contains(listName);
        }
        return false;
    }

    public boolean hasListAccess(Authentication authentication, Long listId) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            Set<Role> roles = user.get().getRoles();
            List<Long> list = roles.stream().map(Role::getId).collect(Collectors.toList());
            return list.contains(listId);
        }
        return false;
    }
}

