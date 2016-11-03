package com.test.main;

import com.test.calculator.entitites.CalcResult;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Gabor on 2016.11.01..
 */
public class ResultProcessor implements Runnable {
    final CompletionListener completionListener;
    final BlockingQueue<CalcResult> resultQueue;

    public ResultProcessor(CompletionListener completionListener, BlockingQueue<CalcResult> resultQueue) {
        this.completionListener = completionListener;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("ResultProcessor");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                CalcResult result = resultQueue.take();
                updateProcessingStatus();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("--------------------- " + Thread.currentThread().getName() + " Thread interrupted, exiting.");
    }

    private void updateProcessingStatus() {
        if (MainThread.processedPositions.incrementAndGet() == MainThread.feededPositions.get()) {
            completionListener.processingComplete();
        }
    }
}
