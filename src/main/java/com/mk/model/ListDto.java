package com.mk.model;

import com.mk.constants.ErrorConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ListDto implements Serializable {
    private Long id;
    @Max(value = 100, message = ErrorConstants.LINE_NAME_VALIDATION_MESSAGE)
    private String name;
}
