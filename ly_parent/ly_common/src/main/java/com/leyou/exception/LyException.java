package com.leyou.exception;

import com.leyou.exception.enums.ResponseCode;
import lombok.Getter;

@Getter
public class LyException extends RuntimeException {
    private Integer status;
    public LyException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.status = responseCode.getStatus();
    }

    public LyException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMessage(), cause);
        this.status = responseCode.getStatus();
    }

    public LyException(int i, String msg) {
        super(msg);
        status = i;
    }
}
