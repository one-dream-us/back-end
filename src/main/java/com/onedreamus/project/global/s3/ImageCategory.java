package com.onedreamus.project.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageCategory {

    THUMBNAIL("thumbnail");

    private final String name;
}
