package com.mk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lists")
@Getter
@Setter
@NoArgsConstructor
public class TodoLists {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false, length = 100)
	private String name;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "todoLists")
	private Set<User> users = new HashSet<>();
	@OneToMany(mappedBy = "list")
	private List<ToDo> task;
}
