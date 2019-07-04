package com.zqj.boot.pattern.adapter.duixiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.duixiang
 * @description Test
 * @create 2019-07-04 20:11
 */
public class Test {

    @org.junit.Test
    public void fun(){
        Object[] objects = new Object[]{"1","2"};
        //本身不具备排序，但是有了适配器，就具备了排序功能。
        Score score = new ScoreAdapter();
        Object[] sortObjects = score.sort(objects);
        System.out.println(sortObjects);
    }
}
