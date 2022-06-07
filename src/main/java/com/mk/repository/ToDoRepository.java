package com.mk.repository;

import com.mk.entity.ToDo;
import com.mk.model.ToDoDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {

  List<ToDoDto> findAllByListId(Long listId);
}
