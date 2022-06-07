package com.mk.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ToDoDto implements Serializable {

  private final Long id;
  private final String title;
  private final Boolean isDone;
  private final LocalDateTime completedDate;
}
