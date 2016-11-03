package com.test.main;

import com.test.calculator.Calculator;
import com.test.calculator.entitites.CalcResult;
import com.test.calculator.entitites.ErrorResult;
import com.test.calculator.entitites.PositionInput;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gabor on 2016.11.01..
 */
public class MainThread {
    public static final AtomicInteger feededPositions = new AtomicInteger(0);
    public static final AtomicInteger processedPositions = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {


        // Hazelcast init queues ------
        final BlockingQueue<PositionInput> inputQueue = new LinkedBlockingQueue<>(10);
        final BlockingQueue<CalcResult> resultQueue = new LinkedBlockingQueue<>(10);
        final BlockingQueue<ErrorResult> errorQueue = new LinkedBlockingQueue<>(10);
        // ---------------------------

        // Start calc side: init API call --------------
        final Calculator calculator = new Calculator(inputQueue, resultQueue, errorQueue);
        calculator.startCalculator();

        final CompletionListener completionListener = new CompletionListener();

        // Init result and input processor threads on main side and start feeding
        // Create different pools for each
        final ExecutorService feederPool = Executors.newFixedThreadPool(1);
        final ExecutorService resultProcessorPool = Executors.newFixedThreadPool(1);
        final ExecutorService errorProcessorPool = Executors.newFixedThreadPool(1);

        feederPool.execute(new InputFeeder(inputQueue));
        resultProcessorPool.execute(new ResultProcessor(completionListener, resultQueue));
        errorProcessorPool.execute(new ErrorProcessor(completionListener, errorQueue));

        // When to stop
        completionListener.waitForCompletion();

        System.out.println("All feeded: " + feededPositions.get() + " processed: " + processedPositions.get());

        // Stop calc processors through API call
        calculator.stopCalculator();

        // Stop input feeder, result and error processor
        stopLocalExecutors(feederPool, resultProcessorPool, errorProcessorPool);

    }


    private static void stopLocalExecutors(ExecutorService feederPool, ExecutorService resultProcessorPool, ExecutorService errorProcessorPool) {
        feederPool.shutdownNow();
        resultProcessorPool.shutdownNow();
        errorProcessorPool.shutdownNow();
    }
}

