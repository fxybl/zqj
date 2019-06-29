package com.zqj.boot.pattern.singleton;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.singleton
 * @description Singleton2懒汉模式
 * @create 2019-06-29 16:46
 */
public class Singleton2 {

    private static Singleton2 singleton;
    //防止通过反射强制实例化
    private static boolean flag = true;

    private Singleton2(){
      if(flag){
          //可重入锁
          synchronized (Singleton2.class){
              if(flag){
                  flag = false;
              }
          }
      }else{
          throw new RuntimeException("单例被破坏");
      }

    }

    public static Singleton2 getInstance(){
        if(singleton==null){
            synchronized (Singleton2.class){
                if(singleton==null){
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }

    //此方法防止序列化破坏单例
    public Object readResolve(){
        return singleton;
    }
}
