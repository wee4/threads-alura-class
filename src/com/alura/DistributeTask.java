package com.alura;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class DistributeTask implements Runnable {

    private final ExecutorService threadPool;
    private final Socket socket;
    private final ServerTask serverTask;
    private final BlockingQueue<String> queue;

    public DistributeTask(ExecutorService threadPool, Socket socket, ServerTask serverTask, BlockingQueue<String> queue) {
        this.threadPool = threadPool;
        this.socket = socket;
        this.serverTask = serverTask;
        this.queue = queue;
    }

    @Override
    public void run() {
        out.println("Distributing task to: " + socket);

        try {
            Scanner sc = new Scanner(socket.getInputStream());
            PrintStream ps = new PrintStream(socket.getOutputStream());

            while (sc.hasNextLine()) {
                String command = sc.nextLine();
                out.println(command);

                switch (command) {
                    case "c1":
                        ps.println("C1 Confirmed");
                        threadPool.execute(new CommandC1(ps));
                        break;
                    case "c2":
                        ps.println("C2 Confirmed");
                        Future<String> futureWs = threadPool.submit(new CommandC2CallWS(ps));
                        Future<String> futureDb = threadPool.submit(new CommandC2AccessDatabase(ps));


                        threadPool.submit(() -> {
                            try {
                                out.println("Waiting results...");
                                ps.println("WS result: " + futureWs.get(10, TimeUnit.SECONDS));
                                ps.println("DB result: " + futureDb.get(10, TimeUnit.SECONDS));
                            } catch (Exception e) {
                                out.println("Stopping c2 execution..");
                                ps.println("Timeout on c2 command");
                                futureWs.cancel(true);
                                futureDb.cancel(true);
                            }

                            out.println("Finish");
                        });
                        break;
                    case "c3":
                        ps.println("C3 Added on queue");
                        this.queue.put(command);
                        break;
                    case "end":
                        ps.println("Shutdown server...");
                        serverTask.stop();
                        break;
                    default:
                        ps.println("Command not found");
                        break;
                }
            }

            ps.close();
            sc.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
