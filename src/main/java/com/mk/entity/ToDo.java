package com.mk.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "to_dos")
@Getter
@Setter
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    private Boolean isDone;
    private LocalDateTime completedDate;
    @ManyToOne
    @JoinColumn(name = "list_name_id")
    private TodoLists list;
}
