package com.zqj.boot.pattern.bridge;

import lombok.Setter;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.bridge
 * @description Abstraction抽象的基层，通过设值注入实现。
 * @create 2019-07-05 09:41
 */

@Setter
public abstract class Abstraction {

    protected Implementor implementor;

    public abstract void draw();


}
