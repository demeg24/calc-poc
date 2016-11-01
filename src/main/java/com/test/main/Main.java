package com.test.main;

import com.test.calculator.Calculator;
import com.test.calculator.entitites.CalcResult;
import com.test.calculator.entitites.ErrorResult;
import com.test.calculator.entitites.PositionInput;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Gabor on 2016.11.01..
 */
public class Main {


    public static void main(String[] args) throws InterruptedException {

        // Hazelcast init queues ------
        final BlockingQueue<PositionInput> inputQueue = new LinkedBlockingQueue<>(10);
        final BlockingQueue<CalcResult> resultQueue = new LinkedBlockingQueue<>(10);
        final BlockingQueue<ErrorResult> errorQueue = new LinkedBlockingQueue<>(10);
        // ---------------------------

        // Start calc side: init API call --------------
        final Calculator calculator = new Calculator(inputQueue, resultQueue, errorQueue);
        calculator.createAndStartWorkers();


        // Init result and input processor threads on main side and start feeding
        // Create different pools for each
        final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);
        threadPoolExecutor.execute(new InputFeeder(inputQueue));
        threadPoolExecutor.execute(new ResultProcessor(resultQueue));
        threadPoolExecutor.execute(new ErrorProcessor(errorQueue));


        // When to stop??? poison pill? ----------------------------
        // Stop calc processors through API call
        calculator.gracefulShutDownWorkers();

        // Stop input feeder, result and error processor
        stopLocalExecutor(threadPoolExecutor);

        System.out.println("Error results processed: " + ErrorProcessor.errorCounter.get());
        System.out.println("Normal results processed: " + ResultProcessor.resultCounter.get());
        System.out.println("All processed: " + (ResultProcessor.resultCounter.get() + ErrorProcessor.errorCounter.get()));
    }

    private static void stopLocalExecutor(ExecutorService threadPoolExecutor) {
        try {
            if (!threadPoolExecutor.awaitTermination(15, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
