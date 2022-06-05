package com.mk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, updatable = false, length = 100)
    private String email;
    @Column(nullable = false, unique = true)
    private String password;
    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 10)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = {
                @JoinColumn(name = "user_id", nullable = false, updatable = false)},
                inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)})
    private Set<Role> roles = new HashSet<>();

    private void addRole(Role role) {
        this.roles.add(role);
    }
}
