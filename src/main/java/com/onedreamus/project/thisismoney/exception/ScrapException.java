package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class ScrapException extends CustomException {

    public ScrapException(ErrorCode errorCode) {
        super(errorCode);
    }
}
