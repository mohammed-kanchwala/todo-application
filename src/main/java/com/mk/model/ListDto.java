package com.mk.model;

import com.mk.constants.ErrorConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ListDto implements Serializable {
    private Long id;
    @Size(max = 100, message = ErrorConstants.LINE_NAME_VALIDATION_MESSAGE)
    private String name;
}
