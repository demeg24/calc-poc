package com.test.calculator;

import com.test.calculator.entitites.CalcResult;
import com.test.calculator.entitites.ErrorResult;
import com.test.calculator.entitites.PositionInput;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gabor on 2016.10.30..
 */
public class Calculator {
    final int poolSize = 4;
    ExecutorService threadPoolExecutorService = Executors.newFixedThreadPool(poolSize);

    private final BlockingQueue<PositionInput> inputQueue;
    private final BlockingQueue<CalcResult> resultQueue;
    private final BlockingQueue<ErrorResult> errorQueue;

    public Calculator(BlockingQueue<PositionInput> inputQueue, BlockingQueue<CalcResult> resultQueue, BlockingQueue<ErrorResult> errorQueue) {
        this.inputQueue = inputQueue;
        this.resultQueue = resultQueue;
        this.errorQueue = errorQueue;
    }

    public void createAndStartWorkers() {
        for (int i = 0; i < poolSize; i++) {
            threadPoolExecutorService.submit(new CalculatorWorker(inputQueue, resultQueue, errorQueue, i));
        }
    }

    /**
     * Called from outside whenever needed.
     */
    public void gracefulShutDownWorkers() {
        try {
            if (!threadPoolExecutorService.awaitTermination(15, TimeUnit.SECONDS)) {
                threadPoolExecutorService.shutdownNow();
            }

            // Create error tasks from pending positions and put it to queue

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
