package com.zqj.boot.pattern.sort;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.sort
 * @description Arrays
 * @create 2019-06-29 20:25比较排序
 */
public class Arrays {

    public static void sort(int[] array,String order){
        //desc降序，asc升序
        for(int i = 0;i<array.length-1;i++){
            for(int j = i+1;j<array.length;j++){
                if("asc".equals(order)){
                    if(array[i]>array[j]){
                        swap(i,j,array);
                    }
                }else {
                    if(array[i]<array[j]){
                        swap(i,j,array);
                    }
                }


            }
        }
    }

    public static void swap(int a,int b,int[] array){
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static void print(int[] array){
        System.out.print("[");
        for(int i=0;i<array.length;i++){
            if(i<array.length-1){
                System.out.print(array[i]+",");
            }else {
                System.out.print(array[i]);
            }

        }
        System.out.println("]");
    }
}
