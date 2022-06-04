package com.mk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "TO_DO")
@Data
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String listName;
    @Column(nullable = false, length = 500)
    private String title;
    private Boolean isDone;
    private LocalDateTime completedDate;
    @Column(nullable = false, updatable = false)
    private String createdBy;
    @Column(nullable = false, updatable = false)
    private String createdOn;
}
