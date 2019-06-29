package com.zqj.boot.pattern.iterator;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description Test
 * @create 2019-06-29 13:23
 */
public class Test {

    @org.junit.Test
    public void fun(){
        ArrayList<Person> arrayList = new ArrayList();
        LinkedList<Person> linkedList = new LinkedList<>();
        for(int i = 11; i<31; i++){
            Person p = new Person(i+"岁的小孩",i);
            arrayList.add(p);
            linkedList.add(p);
        }
        System.out.println(arrayList.size());
        System.out.println(linkedList.size());

        System.out.println("------------------arrayList正序");

        Iterator<Person> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("------------------arrayList倒序");
        Iterator<Person> iterator2 = arrayList.iterator();
        while (iterator2.hasPrevious()){
            System.out.println(iterator2.previous());
        }

        System.out.println("-------------------linkedList正序");
        Iterator<Person> iterator3 = linkedList.iterator();
        while (iterator3.hasNext()){
            System.out.println(iterator3.next());
        }

        System.out.println("-------------------linkedList倒序");
        Iterator<Person> iterator4 = linkedList.iterator();
        while (iterator4.hasPrevious()){
            System.out.println(iterator4.previous());
        }

    }

    @org.junit.Test
    public void fun2(){
        ArrayList<Person> arrayList = new ArrayList();
        long start = System.currentTimeMillis();
        for(int i = 0; i<1000000; i++){
            Person p = new Person(i+"岁的小孩",i);
            arrayList.add(p);
        }
        long end = System.currentTimeMillis();
        System.out.println("arrayList添加总共耗时"+(end-start)+"毫秒");
        LinkedList<Person> linkedList = new LinkedList<>();
        Long start2 = System.currentTimeMillis();
        for(int i = 0; i<1000000; i++){
            Person p = new Person(i+"岁的小孩",i);
            linkedList.add(p);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("linkedList添加总共耗时"+(end2-start2)+"毫秒");

        long start3 = System.currentTimeMillis();
        Iterator<Person> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            iterator.next();
        }
        long end3 = System.currentTimeMillis();
        System.out.println("arrayList遍历耗时"+(end3-start3)+"毫秒");

        long start4 = System.currentTimeMillis();
        Iterator<Person> iterator2 = linkedList.iterator();
        while (iterator2.hasNext()){
            iterator2.next();
        }
        long end4 = System.currentTimeMillis();
        System.out.println("linkedList遍历耗时"+(end4-start4)+"毫秒");

    }
}
