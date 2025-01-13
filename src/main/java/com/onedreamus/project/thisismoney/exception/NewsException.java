package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class NewsException extends CustomException {
    public NewsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
