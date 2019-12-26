package com.zqj.controller;

import com.zqj.common.group.AGroup;
import com.zqj.common.group.BGroup;
import com.zqj.common.pojo.Student;
import com.zqj.common.resp.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zqj
 * @create 2019-12-26 18:11
 */

@RestController
@Validated
public class ExceptionController {

    @PostMapping("/exception/test")
    public Result<String> test(@NotBlank(message = "电话号码不可为空") String phone){
        Result<String> result  = new Result<>();
        result.setMessage("查询成功");
        result.setData("110");
        return result;
    }

    @PostMapping("/exception/test2")
    public Result<String> test2(@NotBlank(message = "名字不可为空") String name,@NotNull(message = "年龄不可为空") Integer age){
        Result<String> result  = new Result<>();
        result.setMessage("查询成功");
        result.setData(name);
        return result;
    }

    @PostMapping("/exception/test3")
    public Result<String> test3(){
        throw new RuntimeException("跑不动了");
    }

    //检验年龄
    @PostMapping("/exception/test4")
    public Result<String> test4(@Validated({AGroup.class}) Student student){
        Result<String> result = new Result<>();
        result.setMessage("查询成功");
        result.setData(student.getAge()+"");
        return result;
    }

    //检验名字
    @PostMapping("/exception/test5")
    public Result<String> test5(@Validated({BGroup.class}) Student student){
        Result<String> result = new Result<>();
        result.setMessage("查询成功");
        result.setData(student.getName()+"");
        return result;
    }
}
