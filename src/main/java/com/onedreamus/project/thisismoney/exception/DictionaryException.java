package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class DictionaryException extends CustomException {

    public DictionaryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
