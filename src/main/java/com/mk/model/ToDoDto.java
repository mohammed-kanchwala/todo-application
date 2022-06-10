package com.mk.model;

import com.mk.constants.ErrorConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ToDoDto implements Serializable {

    private Long id;
    @Max(value = 100, message = ErrorConstants.TASK_TITLE_VALIDATION_MESSAGE)
    private String title;
    private Boolean isDone;
    private LocalDateTime completedDate;
}
