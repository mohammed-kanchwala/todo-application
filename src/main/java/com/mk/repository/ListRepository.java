package com.mk.repository;

import com.mk.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List, Long> {

    Optional<List> findByName(String name);

  Optional<List> findByIdAndNameNot(Long id, String user);
}
