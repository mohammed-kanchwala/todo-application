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
@AllArgsConstructor
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
    private Set<List> lists = new HashSet<>();

    private void addList(List list) {
        this.lists.add(list);
    }
}
