package com.mingri.prize.commons.threadpool.support.eager;

import com.mingri.prize.commons.utils.ThreadUtil;

import java.lang.reflect.Proxy;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 快速消费线程池
 */
public class EagerThreadPoolExecutor extends ThreadPoolExecutor {

    public EagerThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit,
                                   TaskQueue<Runnable> workQueue,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);

        //内部创建代理拒绝策略类
        RejectedExecutionHandler rejectedExecutionHandler = (RejectedExecutionHandler) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                handler.getClass().getInterfaces(),
                new RejectedExecutionProxyInvocationHandler(handler, this)
        );

        setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    /**
     * 拒绝策略次数统计
     */
    private final AtomicInteger rejectCount = new AtomicInteger();


    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }


    /**
     * 设置拒绝次数自增
     */
    public void incrementRejectCount() {
        rejectCount.incrementAndGet();
    }


    /**
     * 获取拒绝次数
     */
    public int getRejectCount() {
        return rejectCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException ex) {
            TaskQueue taskQueue = (TaskQueue) super.getQueue();
            try {
                // 立即尝试将任务放入队列，而不会阻塞等待队列有空闲空间。
                if (!taskQueue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", ex);
                }
            } catch (InterruptedException iex) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(iex);
            }
        } catch (Exception ex) {
            submittedTaskCount.decrementAndGet();
            throw ex;
        }
    }



    /**
     * 快速消费线程池测试
     **/
    public static void main(String[] args) {
        AbortPolicy abortPolicy = new AbortPolicy();
        TaskQueue taskQueue = new TaskQueue<>(1);
        EagerThreadPoolExecutor eagerThreadPoolExecutor =
                new EagerThreadPoolExecutor(1, 3,
                        1024, TimeUnit.SECONDS, taskQueue, abortPolicy);
        taskQueue.setExecutor(eagerThreadPoolExecutor);
        for (int i = 0; i < 5; i++) {
            try {
                eagerThreadPoolExecutor.execute(() -> ThreadUtil.sleep(100000L));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        System.out.println("================ 线程池拒绝策略执行次数: " + eagerThreadPoolExecutor.getRejectCount());
    }
}
