package com.mk.repository;

import com.mk.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    public Role findByName(String name);
}