package com.alura;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 12345)) {
            out.println("Successfully connected");

            Thread commandSender = new Thread(() -> {
                out.println("Can send commands");
                try (Scanner scIn = new Scanner(System.in)) {
                    try (PrintStream ps = new PrintStream(socket.getOutputStream())) {
                        ps.println("Client: " + socket.getLocalPort() + " joined!");
                        while (scIn.hasNextLine()) {
                            String line = scIn.nextLine();
                            if (line.trim().equals("")) {
                                break;
                            }

                            ps.println(line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            });

            Thread commandReceive = new Thread(() -> {
                try (Scanner sc = new Scanner(socket.getInputStream())) {
                    out.println("Receiving data from server");
                    while (sc.hasNextLine()) {
                        out.println(sc.nextLine());
                    }
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            });

            commandSender.start();
            commandReceive.start();

            commandSender.join();

            out.println("Closing socket");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
