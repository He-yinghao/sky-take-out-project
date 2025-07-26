package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    // 处理业务逻辑异常
    public Result<BaseException> exceptionHandler(BaseException ex) {
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    // 处理数据库异常
    public Result<BaseException> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // Duplicate entry '58962' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            return Result.error(username + MessageConstant.ALREADY_EXISTS);
        }
        else return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
