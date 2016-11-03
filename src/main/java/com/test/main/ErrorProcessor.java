package com.test.main;

import com.test.calculator.entitites.ErrorResult;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Gabor on 2016.11.01..
 */
public class ErrorProcessor implements Runnable {
    final CompletionListener completionListener;
    final BlockingQueue<ErrorResult> errorQueue;

    public ErrorProcessor(CompletionListener completionListener, BlockingQueue<ErrorResult> errorQueue) {
        this.completionListener = completionListener;
        this.errorQueue = errorQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("ErrorProcessor");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                ErrorResult errorResult = errorQueue.take();
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
