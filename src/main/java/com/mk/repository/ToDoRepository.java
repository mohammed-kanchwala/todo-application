package com.mk.repository;

import com.mk.entity.ToDo;
import com.mk.entity.TodoLists;
import com.mk.model.ToDoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

  List<ToDo> findAllByListId(Long listId);

  ToDo findByTitle(String title);

//  @Modifying
//  @Query("DELETE from ToDo t where list := list")
//    void deleteAllByListId(TodoLists list);
}
