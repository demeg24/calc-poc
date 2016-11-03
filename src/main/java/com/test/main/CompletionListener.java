package com.test.main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Listener for waiting for all the positions to be processed.
 */
public class CompletionListener {
    final Lock lockObject = new ReentrantLock();
    final Condition processingFinished = lockObject.newCondition();

    public void waitForCompletion() throws InterruptedException {
        lockObject.lock();
        try {
            System.out.println("CompletionListener: waiting for completion.");
            processingFinished.await();
        } catch (InterruptedException e) {
            throw e;
        } finally {
            lockObject.unlock();
        }
    }

    public void processingComplete() {
        lockObject.lock();
        try {
            System.out.println("CompletionListener: processing complete.");
            processingFinished.signalAll();
        } finally {
            lockObject.unlock();
        }
    }
}
