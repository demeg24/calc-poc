package com.test.main;

import com.test.calculator.entitites.CalcResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gabor on 2016.11.01..
 */
public class ResultProcessor implements Runnable {
    public static final AtomicInteger resultCounter = new AtomicInteger(0);

    final BlockingQueue<CalcResult> resultQueue;

    public ResultProcessor(BlockingQueue<CalcResult> resultQueue) {
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("ResultProcessor");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                CalcResult result = resultQueue.take();
//                System.out.println("Result processed: " + result.getResult() + " queue size: " + resultQueue.size());
                resultCounter.getAndIncrement();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("---------------------" + Thread.currentThread().getName() + " Thread interrupted, exiting.");
    }
}
