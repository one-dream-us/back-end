package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class KeyNoteException extends CustomException {
    public KeyNoteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
