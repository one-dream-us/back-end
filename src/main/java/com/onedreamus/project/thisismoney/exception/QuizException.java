package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class QuizException extends CustomException {

    public QuizException(ErrorCode errorCode) {
        super(errorCode);
    }
}
