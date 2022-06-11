package com.mk.repository;

import com.mk.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

	List<ToDo> findAllByListId(Long listId);

	ToDo findByTitle(String title);

//  @Modifying
//  @Query("DELETE from ToDo t where list := list")
//    void deleteAllByListId(TodoLists list);
}
