package com.zqj.boot.pattern.iterator;

/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description LinkedList
 * @create 2019-06-29 12:18
 */
public class LinkedList<E> implements  Collection<E>{
    //头元素
    private  Node head;
    //尾元素
    private  Node tail;
    //集合的长度
    private  int  index = 0;

    @Override
    public void add(E e) {
        //创建一个新的节点
        Node next = new Node(e,null,null);
        //如果是第一次添加
        if(head==null){
            //头和尾元素都指向此节点,下一个元素都为空,上一个元素也为空
            head = next;
            tail = next;
        }else{
            //如果不是第一次添加，尾元素成为了倒数第2个元素，将其下一个元素设置为当前新的元素，同时将自己指向新的元素，也就是最后一个元素.
            tail.setNext(next);
            Node<E> previous = tail;
            tail = next;
            tail.setPrevious(previous);
        }
        //长度+1
        index++;

    }

    @Override
    public int size() {
        return index;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator<E>();
    }


    public class LinkedListIterator<E> implements Iterator<E>{
        //当前元素默认为尾元素
        private Node<E> currentNodeDesc = tail;
        //当前元素为头元素
        private Node<E> currentNodeAsc = head;


        @Override
        public boolean hasNext() {
            //判断是否有下一个节点
            if(currentNodeAsc ==null){
                return false;
            }
            return true;
        }

        @Override
        public E next() {
            //直接返回当前节点的下一个节点的数据
            E now = currentNodeAsc.getData();
            if(currentNodeAsc!=null){
                currentNodeAsc = currentNodeAsc.getNext();
                return now;
            }
            return null;

        }

        @Override
        public boolean hasPrevious() {
            //判断是否有上一个节点
            if(currentNodeDesc==null){
                return false;
            }
            return true;
        }

        @Override
        public E previous() {
            //直接返回当前节点的上一个节点的数据
            if(currentNodeDesc!=null){
                E e = currentNodeDesc.getData();
                currentNodeDesc = currentNodeDesc.getPrevious();
                return e;
            }
            return null;

        }
    }
}
