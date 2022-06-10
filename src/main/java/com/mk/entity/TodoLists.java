package com.mk.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoLists {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "todoLists")
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "list")
    private List<ToDo> task;
}