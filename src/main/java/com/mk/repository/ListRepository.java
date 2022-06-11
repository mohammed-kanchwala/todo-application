package com.mk.repository;

import com.mk.entity.TodoLists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<TodoLists, Long> {

	Optional<TodoLists> findByName(String name);

	Optional<TodoLists> findByIdAndNameNot(Long id, String user);
}
