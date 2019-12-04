package com.zqj.jdk.aqs;

import java.io.Serializable;

/**
 * @author zqj
 * @create 2019-12-04 11:02
 */
public abstract class MyAbstractQueuedSynchronizer implements Serializable {

    private static final long serialVersionUID = 7373984972572414692L;

    /*
     *链表结构
    *   * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
    *
    * */


}
