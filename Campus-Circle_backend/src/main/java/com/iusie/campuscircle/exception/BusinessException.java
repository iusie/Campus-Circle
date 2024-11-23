package com.iusie.campuscircle.exception;


import com.iusie.campuscircle.common.StateCode;

public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;

    public BusinessException(String message,int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(StateCode stateCode) {
        super(stateCode.getMessage());
        this.code = stateCode.getCode();
        this.description = stateCode.getDescription();
    }
    public BusinessException(StateCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
