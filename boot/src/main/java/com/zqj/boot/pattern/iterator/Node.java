package com.zqj.boot.pattern.iterator;


/**
 * @author zqj
 * @program com.zqj.boot.pattern.iterator
 * @description Node
 * @create 2019-06-29 12:21
 */

public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> previous;

    public Node(E data,Node<E> next,Node<E> previous){
        this.data = data;
        this.next = next;
    }

    public E getData(){
        return data;
    }

    public Node<E> getNext(){
        return next;
    }

    public void setNext(Node<E> next){
        this.next = next;
    }

    public Node<E> getPrevious(){
        return previous;
    }

    public void setPrevious(Node<E> previous){
        this.previous = previous;
    }
}
