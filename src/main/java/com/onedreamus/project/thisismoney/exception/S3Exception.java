package com.onedreamus.project.thisismoney.exception;

import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;

public class S3Exception extends CustomException {
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}
