package com.github.baserecycleradapter;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2); caption

        Callable<Integer> myCallable = new MyCallable();
        FutureTask<Integer> ft = new FutureTask<Integer>(myCallable);

        System.out.println("------" + Thread.currentThread().getName() + " ");

        Thread t = new Thread(ft);
        t.start();


        try {

            int sum = ft.get();

            System.out.println("" + sum);

        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }


    }
}