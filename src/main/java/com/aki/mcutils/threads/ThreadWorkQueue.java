package com.aki.mcutils.threads;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadWorkQueue extends LinkedBlockingQueue<Runnable> implements RejectedExecutionHandler {
    private AtomicInteger idleThreads = new AtomicInteger(0);

    @Override
    public boolean offer(Runnable runnable) {
        if(idleThreads.get() == 0) {
            return false;
        } else return super.offer(runnable);
    }

    @Override
    public Runnable take() {
        idleThreads.incrementAndGet();
        try {
            return super.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            idleThreads.decrementAndGet();
        }
        return null;
    }

    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        idleThreads.incrementAndGet();
        try {
            return super.poll(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            idleThreads.decrementAndGet();
        }
        return null;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Task $r rejected from $executor");
        }
        this.offer(r);
    }
}
