package com.mk.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, updatable = false, length = 100)
    private String email;
    @Column(nullable = false, unique = true)
    private String password;
    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 10)
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_lists",
            joinColumns = {
                @JoinColumn(name = "user_id", nullable = false, updatable = false)},
                inverseJoinColumns = {@JoinColumn(name = "list_id", nullable =
                  false, updatable = false)})
    private Set<TodoLists> todoLists = new HashSet<>();

    private void addList(TodoLists todoLists) {
        this.todoLists.add(todoLists);
    }
}
