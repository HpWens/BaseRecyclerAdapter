package com.github.baserecycleradapter;

import java.util.concurrent.Callable;

/**
 * Created by boby on 2017/1/23.
 */

public class MyCallable implements Callable<Integer> {

    private int i = 0;

    @Override
    public Integer call() throws Exception {

        int sum = 0;

        for (; i < 100; i++) {

            System.out.println("****" + Thread.currentThread().getName() + " " + i);

            sum += i;
        }

        return sum;
    }
}
