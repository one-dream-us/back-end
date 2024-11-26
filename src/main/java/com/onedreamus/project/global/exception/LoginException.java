package com.onedreamus.project.global.exception;

public class LoginException extends CustomException{
    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
