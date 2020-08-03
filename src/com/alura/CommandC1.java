package com.alura;

import java.io.PrintStream;

import static java.lang.System.out;

public class CommandC1 implements Runnable {
    private final PrintStream outPut;

    public CommandC1(PrintStream outPut) {
        this.outPut = outPut;
    }

    @Override
    public void run() {
        out.println("Executing c1...");
        try {
            Thread.sleep(20000);

            out.println("Command c1 successfully executed !!");
            outPut.println("Hi client, Command c1 successfully executed !!");
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }


}
