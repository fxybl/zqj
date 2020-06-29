package com.zqj.boot.lambda_steam;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zqj
 * @create 2020-06-29 15:14
 */
public class Lambda {

    public List<Integer> list = Lists.newArrayList(11,11,null,2,3,4,null,5,6,7,8,9,10);

    /**
     * Lambda表达式
     */
    @Test
    public void fun(){
        //这会打印2次，因为不同的流了
       list.stream().forEach(System.out::println);
       list.stream().forEach(i -> System.out.println(i));
        System.out.println("---------------------");
        //使用了同一个流，打印第2次时，会抛出异常，一个流只会被打印一次
        Stream<Integer> stream = list.stream();
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);
    }

    /**
     * Stream函数式操作流元素集合
     */
    @Test
    public void fun2(){
        //通过Stream将字符串转换为int
        List<String> stringList = Lists.newArrayList("1","2","3");
        List<Integer> integerList = stringList.stream().map(i ->Integer.parseInt(i)).collect(Collectors.toList());
        System.out.println(integerList);

        int numbers = list.stream() //先转换为流
        .filter(i -> i !=null)  //过滤空的
        .distinct()   //去重
        .mapToInt(i -> i*2)  //map操作
        .sorted()   //排序
        .skip(2)  //跳过前2个元素
        .limit(4)  //只取前4个
        .peek(System.out::println)   //流式处理函数
        .sum();   //求和
        System.out.println("总和是:"+numbers);
        //将流转换为其他类型
        Set<Integer> collect = list.stream().collect(Collectors.toSet());
        collect.forEach(i -> System.out.println(i));

    }
}
