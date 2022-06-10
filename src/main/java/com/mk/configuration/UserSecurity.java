package com.mk.configuration;

import com.mk.constants.ApplicationConstants;
import com.mk.entity.List;
import com.mk.entity.User;
import com.mk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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
            Set<List> lists = null;
            lists = user.get().getLists();
            java.util.List<String> list = lists.stream().map(List::getName).collect(Collectors.toList());
            list.removeIf(s -> s.equalsIgnoreCase(ApplicationConstants.USER_ROLE));
            return list.contains(listName);
        }
        return false;
    }

    public boolean hasListAccess(Authentication authentication, Long listId) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            Set<List> lists = user.get().getLists();
            lists.removeIf(l -> l.getName().equalsIgnoreCase(ApplicationConstants.USER_ROLE));
            java.util.List<Long> list = lists.stream().map(List::getId).collect(Collectors.toList());
            return list.contains(listId);
        }
        return false;
    }
}

