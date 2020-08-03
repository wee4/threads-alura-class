package com.alura;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.out;

public class ServerTask {

    private final ExecutorService threadPool;
    private final ServerSocket serverSocket;
    private final AtomicBoolean isRunning;
    private final BlockingQueue<String> queue;

    public ServerTask() throws IOException {
        out.println("Server Started and ready to receive connections ....");
        this.serverSocket = new ServerSocket(12345);
        this.threadPool = Executors.newCachedThreadPool(new MyThreadFactory());
        this.isRunning = new AtomicBoolean(true);
        this.queue = new ArrayBlockingQueue<>(2);
        this.initConsumer(2);
    }

    public static void main(String[] args) throws IOException {
        ServerTask server = new ServerTask();
        server.run();
        server.stop();
    }

    void run() throws IOException {
        while (this.isRunning.get()) {
            try {
                Socket socket = this.serverSocket.accept();
                out.println("new Client connected on port: " + socket.getPort());
                DistributeTask dt = new DistributeTask(threadPool, socket, this, queue);
                threadPool.execute(dt);
            } catch (SocketException e) {
                out.println("Socket Closed...");
            }
        }

    }

    void stop() throws IOException {
        isRunning.set(false);
        serverSocket.close();
        threadPool.shutdown();
    }

    private void initConsumer(Integer consumers) {
        for (int i = 0; i < consumers; i++) {
            this.threadPool.execute(new ConsumerTask(queue));
        }
    }
}

