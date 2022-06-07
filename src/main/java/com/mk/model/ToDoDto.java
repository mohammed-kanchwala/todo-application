package com.mk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ToDoDto implements Serializable {

  private Long id;
  private String title;
  private Boolean isDone;
  private LocalDateTime completedDate;
}
