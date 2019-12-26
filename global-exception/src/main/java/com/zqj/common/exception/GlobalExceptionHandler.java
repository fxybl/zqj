package com.zqj.common.exception;

import com.zqj.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zqj
 * @create 2019-12-26 18:08
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //@NotNull此类注解抛出此注解
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result<String> validateException(ConstraintViolationException e){
        //每种异常遍历出来返回
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> list = new ArrayList<>();
        for(ConstraintViolation<?> c : constraintViolations){
            list.add(c.getMessageTemplate());
        }
        Result<String> result = new Result<>();
        result.setCode(466);
        result.setMessage(list.toString());
        return result;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> exception(Exception e){
        Result<String> result = new Result<>();
        result.setCode(488);
        result.setMessage(e.getMessage());
        return result;
    }





}
