package com.commenau.util;

import com.commenau.dao.CancelProductDAO;
import com.commenau.dao.ProductDAO;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * This class is used for scheduling.
 * At 23:59 PM at the end of each day,
 * products that still have a quantity greater than 0 will be canceled.
 * Then all products will set up with available equal 50
 */
@WebListener
public class ProductCancellationSchedule implements ServletContextListener {
    @Inject
    private CancelProductDAO cancelDAO;
    @Inject
    private ProductDAO productDAO;
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::cancelProduct, getDelayToNextDay(), TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void cancelProduct() {
        cancelDAO.cancelProduct();
        productDAO.setAvailableToZero();
        System.out.println("Đã hủy các sản phẩm lúc: " + LocalDateTime.now());
    }

    private long getDelayToNextDay() {
        LocalDateTime now = LocalDateTime.now();
        // set time at 23:59 everyday
        LocalDateTime nextRun = now.toLocalDate().atTime(23, 59,59);

        // Calculate the remaining time until 23:59
        long initialDelay = ChronoUnit.SECONDS.between(now, nextRun);
//        System.out.println(initialDelay);
        return initialDelay;
    }


}
