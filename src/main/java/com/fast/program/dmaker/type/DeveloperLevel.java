package com.fast.program.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
    NEW("돋나물"),
    JUNIOR("주니어"),
    JUNGNIOR("중니어"),
    SENIOR("시니어");

    private final String description;
}
