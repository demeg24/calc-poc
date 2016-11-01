package com.test.main;

import com.test.calculator.entitites.CalcResult;
import com.test.calculator.entitites.ErrorResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gabor on 2016.11.01..
 */
public class ErrorProcessor implements Runnable {
    public static final AtomicInteger errorCounter = new AtomicInteger(0);

    final BlockingQueue<ErrorResult> errorQueue;

    public ErrorProcessor(BlockingQueue<ErrorResult> errorQueue) {
        this.errorQueue = errorQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("ErrorProcessor");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                ErrorResult errorResult = errorQueue.take();
//                System.out.println("Error processed: " + errorResult.getPositionId() + " queue size: " + errorQueue.size());
                errorCounter.getAndIncrement();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("---------------------" + Thread.currentThread().getName() + " Thread interrupted, exiting.");
    }
}
