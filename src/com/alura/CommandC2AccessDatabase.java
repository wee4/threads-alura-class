package com.alura;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.System.out;

public class CommandC2AccessDatabase implements Callable<String> {
    private final PrintStream outPut;

    public CommandC2AccessDatabase(PrintStream outPut) {
        this.outPut = outPut;
    }

    @Override
    public String call() throws Exception {
        out.println("Server receive command database c2...");
        outPut.println("Processing command.... database");
        Thread.sleep(15000);

        Integer number = new Random().nextInt(1000) + 1;

        outPut.println("Command c2 - database successfully executed !!");
        out.println("Finish command database - c2...");
        return number.toString();
    }
}
