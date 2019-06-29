package com.zqj.boot.pattern.iterator;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description ArrayList
 * @create 2019-06-28 17:33
 */
public class ArrayList<E> implements Collection<E> {

    //先给定一个数组，长度为10
    private Object [] objects = new Object[10];
    //默认长度为0
    int index = 0;

    @Override
    public void add(E e) {
        //如果此时index的值等于其长度，则自动扩容2倍
        if(index==objects.length){
            Object[] newObjects = new Object[objects.length*2];
            //将全局数组拷贝到新的数组里面
            System.arraycopy(objects,0,newObjects,0,objects.length);
            //将全局数组引用指向新的数组
            objects = newObjects;
        }
        //进行赋值
        objects[index] = e;
        //长度自增长
        index++;

    }

    public class ArrayListIterator<E> implements Iterator<E>{

        //正序遍历
        private int currentIndexAsc = 0;
        //倒序遍历
        private int currentIndexDesc =index;

        @Override
        public boolean hasNext() {
            if(currentIndexAsc==index){
                return false;
            }
            return true;
        }

        @Override
        public E next() {
            if(currentIndexAsc < index){
                E e = (E)objects[currentIndexAsc];
                currentIndexAsc++;
                return e;
            }
            return null;
        }

        @Override
        public boolean hasPrevious() {
            if(currentIndexDesc==0){
                return false;
            }
            return true;
        }

        @Override
        public E previous() {
            if(currentIndexDesc>0){
                E e = (E)objects[currentIndexDesc-1];
                currentIndexDesc--;
                return e;
            }
            return null;
        }
    }

    @Override
    public Iterator iterator() {
            return new ArrayListIterator<E>();
    }

    @Override
    public int size() {
        return index;
    }
}
