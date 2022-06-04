package com.mk.repository;

import com.mk.entity.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {

    List<ToDo> findByListNameAndCreatedBy(String listName, String createdBy);

    @Query("SELECT DISTINCT listName from ToDo")
    List<String> findDistinctListNames();
}