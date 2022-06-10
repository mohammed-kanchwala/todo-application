package com.mk.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class List {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "lists")
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "list")
    private java.util.List<ToDo> task;
}
