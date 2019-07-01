package com.zqj.boot.pattern.strategy;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.strategy
 * @description DataSort
 * @create 2019-07-01 16:28
 */
public class DataSort {

    public static void sort(Comparable[] comparables){
        for(int i = 0; i <comparables.length-1; i++){
            for(int j=i+1; j <comparables.length;j++){
                if(comparables[i].compareTo(comparables[j])>0){
                    swap(i,j,comparables);
                }
            }
        }

    }

    private static void swap(int x,int y,Comparable[] comparables){
        Comparable temp = comparables[x];
        comparables[x] = comparables[y];
        comparables[y] = temp;
    }

    public static  void print(Comparable[] comparables){
        for(Comparable comparable : comparables){
            System.out.println(comparable.toString());
        }
    }
}
