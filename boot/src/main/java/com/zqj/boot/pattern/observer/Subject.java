package com.zqj.boot.pattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.observer
 * @description Subject 主题，订阅号
 * @create 2019-07-05 14:24
 */
public abstract class Subject {

        protected List<Observer> observers = new ArrayList<>();

        public void addObserver(Observer observer){
            observers.add(observer);
        }

        public void removeObserver(Observer observer){
            observers.remove(observer);
        }

        public abstract void notifyObserver();

}
