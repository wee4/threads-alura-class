package com.alura;

import static java.lang.System.out;

public class ExeptionThreadHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        out.println("Throw Exception on thread: " + thread.getName());
        out.println("Message: " + throwable.getMessage());
    }
}
