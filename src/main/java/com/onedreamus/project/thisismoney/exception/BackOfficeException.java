package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class BackOfficeException extends CustomException {
    public BackOfficeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
