package com.onedreamus.project.bank.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class ContentException extends CustomException {

    public ContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
