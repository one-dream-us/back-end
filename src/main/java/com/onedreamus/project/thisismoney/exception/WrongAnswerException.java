package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class WrongAnswerException extends CustomException {
    public WrongAnswerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
