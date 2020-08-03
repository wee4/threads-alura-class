package com.alura;

import java.util.concurrent.BlockingQueue;

public class ConsumerTask implements Runnable {

    private final BlockingQueue<String> queue;

    public ConsumerTask(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String command = null;

            while ((command = queue.take()) != null) {
                System.out.println("Consuming command: " + command + "," + Thread.currentThread().getName());
                Thread.sleep(5000);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("Cant consume");
        }
    }
}
