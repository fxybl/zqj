package com.zqj.jdk.map;


/**
 * @author zqj
 * @create 2019-11-25 11:26
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    //map的数组，初次使用的时候初始化
    private Node<K, V>[] table;

    //map的元素个数
    private int size;

    //默认初始化大小
    private static final int DEFAULT_INTI_CAPACITY = 16;

    //目前数组的大小
    private int threshold;

    //扩容因子，越小，hash冲突越小。0.75最佳,当size > threshold * DEFAULT_LOAD_FACTOR 时进行扩容
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public MyHashMap(int capacity){
        if(capacity <= 0){
            throw new IllegalArgumentException("初始化参数capacity无效");
        }
        this.threshold = capacity;
    }

    public MyHashMap(){
        this(DEFAULT_INTI_CAPACITY);
    }


    @Override
    public V put(K k, V v) {
        //如果map为空则初始化map
        if (table == null) {
            table = new Node[threshold];
        }
        //当size > threshold * DEFAULT_LOAD_FACTOR时进行扩容
        if (size > threshold * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        //计算出数组的下标
        int index = getIndex(k, threshold);
        //获取当前坐标的node
        Node<K, V> node = table[index];
        //为空新建一个node,同时size++
        if (node == null) {
            node = new Node(k, v, null);
            size++;
            return node.getValue();
        } else {
            //首先将newNode赋值为头部的node，Node为链状，
            //firstNode --> node --> tailNode
            Node<K, V> newNode = node;
            //进行遍历，如果找到了一样的则进行更新，否则新增一个
            while (newNode != null) {
                //如果k相同,更新
                if (newNode.getKey().equals(k) || newNode.getKey() == k) {
                    newNode.setValue(v);
                    return newNode.getValue();
                }
                newNode = newNode.getNextNode();
            }
            //如果遍历完仍然没有找到匹对的hash冲突的Key,则新建一个Node并添加到链头
            table[index] = new Node(k, v, newNode);
            size++;
            return table[index].getValue();
        }
    }

    //获取数组下标
    private int getIndex(K k, int length) {
        //取模
        int hashCode = k.hashCode();
        int index = hashCode & length;
        return index;
    }

    //数组重新扩容
    private void resize() {
        //之前的长度* 2
        int newLength = threshold * 2;
        //新建一个Node数组
        Node<K, V>[] newTable = new Node[newLength];
        //对老的table进行遍历，计算出老的key对应的新的table的角标，同时移入新的table,但是此方法会导致链表中的node元素倒置
        for (int i = 0; i < table.length; i++) {
            Node<K, V> oldNode = table[i];
            while (oldNode != null) {
                //gc回收
                table[i] = null;
                K k = oldNode.getKey();
                //新table对应的角标
                int index = getIndex(k, newLength);
                //将旧table[i]的下一个Node赋值暂存
                Node<K, V> nextNode = oldNode.getNextNode();
                //初次newTable[index]为空，因此首个被移位的Node.next为空
                oldNode.setNextNode(newTable[index]);
                //将旧的table中的之前的角标移位到新的table中的新的角标
                newTable[index] = oldNode;
                //赋值老的nextNode,循环遍历
                oldNode = nextNode;
            }
        }
        //将新的table赋值
        table = newTable;
        //赋值新的数组长度
        threshold = newLength;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K k) {
        //获取下标
        int index = getIndex(k, threshold);
        //获取对应下标的链头Node
        Node<K, V> node = table[index];
        //相等直接返回
        if (k == node.getKey() || k.equals(node.getKey())) {
            return node.getValue();
        } else {
            //获取下一个，并进行遍历，如果相等，直接返回
            Node<K, V> next = node.getNextNode();
            while (next != null) {
                if (k == next.getKey() || k.equals(next.getKey())) {
                    return next.getValue();
                }
                next = next.getNextNode();
            }
        }
        return null;
    }

    //table数组的每个元素结构
    class Node<K, V> implements MyMap.Entry<K, V> {

        private K k;

        private V v;

        private Node<K, V> next;

        public Node(K k, V v, Node<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }

        @Override
        public void setValue(V v) {
            this.v = v;
        }

        public Node<K, V> getNextNode() {
            return this.next;
        }

        public void setNextNode(Node<K, V> next) {
            this.next = next;
        }


    }
}
