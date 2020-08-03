package com.alura;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {

    private static Integer counter = 1;

    @Override
    @SuppressWarnings("java:S2696")
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "Server task number: " + counter);
        counter++;
        t.setUncaughtExceptionHandler(new ExeptionThreadHandler());
        return t;
    }
}
