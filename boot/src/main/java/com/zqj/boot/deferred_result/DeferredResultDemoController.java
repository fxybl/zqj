package com.zqj.boot.deferred_result;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * spring自带的异步结果类
 * @author zqj
 * @create 2020-03-01 18:36
 */

@Controller
public class DeferredResultDemoController {

    private static ExecutorService POOL = Executors.newFixedThreadPool(10);

    @RequestMapping("/deferredResult")
    public DeferredResult<String> deferredResult(){
        //3秒没有执行完毕则超时
        DeferredResult<String> deferredResult = new DeferredResult<>(3*1000L);
        deferredResult.onCompletion(() -> System.out.println("执行完成-----"));
        deferredResult.onTimeout(()-> System.out.println("执行超时"));
        POOL.execute(() -> {
            System.out.println("开始执行任务了");
            deferredResult.setResult("任务执行结束了");
        });
        return deferredResult;
    }
}
