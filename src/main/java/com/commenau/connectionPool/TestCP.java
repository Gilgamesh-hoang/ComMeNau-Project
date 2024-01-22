package com.commenau.connectionPool;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;


public class TestCP extends Thread {
    private String taskName;
    private CountDownLatch latch;

    public TestCP(CountDownLatch latch, String taskName) {
        this.taskName = taskName;
        this.latch = latch;
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        int NUMBER_OF_USERS = 20;
        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_USERS);
        for (int i = 1; i <= NUMBER_OF_USERS; i++) {
            Thread worker = new TestCP(latch, "" + i);
            worker.start();
        }
        latch.await();
        System.out.println("DONE All Tasks");
        while (true){

        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Starting. Task = " + taskName);
        execute();
        latch.countDown();
    }

    private void execute() {
        int s = JDBIConnector.getInstance().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) AS total FROM cancel_products").mapTo(Integer.class).one());
        System.out.println("Pool status of task " + taskName + ": " + com.commenau.connectionPool.ConnectionPool.getInstance().toString());
        System.out.println("Task = " + taskName + ": Run SQL successfully " + s);

    }
}