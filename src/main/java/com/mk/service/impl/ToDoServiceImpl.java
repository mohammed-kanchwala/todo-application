package com.mk.service.impl;

import com.mk.repository.ToDoRepository;
import com.mk.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Override
    public List<String> findAllList() {
        return toDoRepository.findDistinctListNames();
    }
}
