package com.mk.configuration;

import com.mk.entity.Role;
import com.mk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("userSecurity")
public class UserSecurity {
    @Autowired
    UserRepository userRepository;

    public boolean hasListAccess(Authentication authentication, String listName) {
        Set<Role> roles = userRepository.findByEmail(authentication.getName()).getRoles();

        return roles.contains(listName);
    }
}
