package com.test.main;

import com.test.calculator.entitites.Position;
import com.test.calculator.entitites.PositionInput;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Gabor on 2016.11.01..
 */
public class InputFeeder implements Runnable {
    final BlockingQueue<PositionInput> inputQueue;

    public InputFeeder(BlockingQueue<PositionInput> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Feeder thread");

        while (!Thread.currentThread().isInterrupted()) {
            try {// Feed the data through hazelcast
                for (int i = 0; i < 8000000; i++) {
                    inputQueue.put(new PositionInput(new Position(i, "desc " + i)));
//                System.out.println("Input added to queue: " + i);
                }
                System.out.println(LocalDateTime.now() + " Stopped feeding, leaving thread.");
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("---------------------" + Thread.currentThread().getName() + " Thread interrupted, exiting.");
    }
}
