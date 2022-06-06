package com.mk.service.impl;

import com.mk.entity.Role;
import com.mk.entity.User;
import com.mk.model.UserDto;
import com.mk.repository.RoleRepository;
import com.mk.repository.UserRepository;
import com.mk.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void register(UserDto user) throws Exception {

        User userEntity = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (Objects.nonNull(userEntity)) {
            throw new Exception();
        }
        userEntity = new User();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(encoder.encode(user.getPassword()));
        Role role = new Role();
        role.setName("USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        userEntity.setRoles(roleSet);
        userRepository.save(userEntity);
    }
}
