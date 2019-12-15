package com.leyou.exception.vo;

import java.util.Date;

public class ExceptionResult {
    private Integer status;
    private String message;
    private Date timestamp;

    public ExceptionResult(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
    public ExceptionResult(int status, String message) {
        this.status=status;
        this.message=message;
        this.timestamp = new Date();
    }
}
