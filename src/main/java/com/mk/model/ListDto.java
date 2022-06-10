package com.mk.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ListDto implements Serializable {
    private Long id;
    private String name;
}
