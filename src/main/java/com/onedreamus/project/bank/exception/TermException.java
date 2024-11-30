package com.onedreamus.project.bank.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class TermException extends CustomException {

    public TermException(ErrorCode errorCode) {
        super(errorCode);
    }
}
