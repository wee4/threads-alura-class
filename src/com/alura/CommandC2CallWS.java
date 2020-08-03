package com.alura;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.System.out;

public class CommandC2CallWS implements Callable<String> {
    private final PrintStream outPut;

    public CommandC2CallWS(PrintStream outPut) {
        this.outPut = outPut;
    }

    @Override
    public String call() throws Exception {
        out.println("Server receive command c2 - WS...");
        outPut.println("Processing command - WS....");
        Thread.sleep(15000);

        Integer number = new Random().nextInt(1000) + 1;

        outPut.println("Command c2 - WS successfully executed !!");
        out.println("Finish command c2 - WS...");
        return number.toString();
    }
}
