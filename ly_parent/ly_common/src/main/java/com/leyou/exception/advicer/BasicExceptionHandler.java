package com.leyou.exception.advicer;

import com.leyou.exception.LyException;
import com.leyou.exception.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice
public class BasicExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> exceptionResultResponseEntity(RuntimeException e) {
        return ResponseEntity.
                status(500).body(new ExceptionResult(500,e.getMessage()));
    }
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> exceptionResultResponseEntity(LyException e) {
        return
                ResponseEntity.
                        status(e.getStatus())
                        .body(new ExceptionResult(e.getStatus(),e.getMessage()));
    }
}
