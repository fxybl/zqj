package com.zqj.boot.pattern.adapter.duixiang;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.adapter.duixiang
 * @description Adapter
 * @create 2019-07-04 20:03
 */
public class ScoreAdapter implements Score {

    private Sort sort;

    public ScoreAdapter(){
        this.sort = new Sort();
    }

    @Override
    public Object[] sort(Object[] objects) {
        //Score本身不具备排序功能，通过排序类Sort引用进来，他就具有了排序功能。
        return sort.quickSort(objects);
    }
}
