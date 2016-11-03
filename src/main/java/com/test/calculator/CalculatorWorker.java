package com.test.calculator;

import com.test.calculator.entitites.CalcResult;
import com.test.calculator.entitites.ErrorResult;
import com.test.calculator.entitites.PositionInput;
import com.test.calculator.entitites.exceptions.ErrorException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Gabor on 2016.10.30..
 */
public class CalculatorWorker implements Runnable {

    private final BlockingQueue<PositionInput> inputQueue;
    private final BlockingQueue<CalcResult> resultQueue;
    private final BlockingQueue<ErrorResult> errorQueue;
    final int i;

    public CalculatorWorker(BlockingQueue<PositionInput> inputQueue, BlockingQueue<CalcResult> resultQueue, BlockingQueue<ErrorResult> errorQueue, int i) {
        this.inputQueue = inputQueue;
        this.resultQueue = resultQueue;
        this.errorQueue = errorQueue;
        this.i = i;

    }

    @Override
    public void run() {
        Thread.currentThread().setName("Calc " + i);
        System.out.println("Starting worker: " + Thread.currentThread().getName());

        while (!Thread.currentThread().isInterrupted()) {

            PositionInput inputPositionInput = null;
            try {
                // 1. start reading input
                inputPositionInput = inputQueue.take();
                // 2. Register calculation task in hazelcast in memory map

                // 3. do calc
                doCalcAndSendResultBack(inputPositionInput);

                // 5. Remove calc status from hazelcast in memory map
                // TODO

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//            } catch (RetryNeededException e) {
//                handleRetry(inputPositionInput);
            } catch (ErrorException e) {
                handleError(inputPositionInput);
            }
        }
        System.out.println("------------------------------------" + Thread.currentThread().getName() + " Thread interrupted, exiting.");
    }

    private void doCalcAndSendResultBack(PositionInput inputPositionInput) throws InterruptedException {

        // 4. create and return result
        int whatToReturn = ThreadLocalRandom.current().nextInt(1, 3);

        switch (whatToReturn) {
            case 1:
                final CalcResult res = new CalcResult(inputPositionInput.getPositionPayload().getPositionId(), "Ok");
                resultQueue.put(res);
                break;
            case 2:
                throw new ErrorException("Error due to failure.");
            default:
                throw new IllegalStateException("Nos such case.");
        }
    }


    private void handleError(PositionInput inputPositionInput) {
        if (inputPositionInput != null) {
            try {
                errorQueue.put(new ErrorResult(inputPositionInput.getPositionPayload().getPositionId(), "error"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
