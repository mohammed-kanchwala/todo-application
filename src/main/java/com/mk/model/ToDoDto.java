package com.mk.model;

import com.mk.constants.ErrorConstants;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ToDoDto implements Serializable {

    private Long id;
    @Size(max = 100, message = ErrorConstants.TASK_TITLE_VALIDATION_MESSAGE)
    private String title;
    private Boolean isDone;
    private LocalDateTime completedDate = LocalDateTime.now();
}
