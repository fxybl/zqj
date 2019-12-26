package com.zqj.common.pojo;

import com.zqj.common.group.AGroup;
import com.zqj.common.group.BGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author zqj
 * @create 2019-12-26 19:03
 */

@Data
public class Student {

    @NotNull(message = "名字不可为空",groups = BGroup.class)
    private String name;

    @Min(message = "小于18岁不可操作",value = 18,groups = AGroup.class)
    private int age;
}
