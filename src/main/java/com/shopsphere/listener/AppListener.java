package com.shopsphere.listener;

import com.shopsphere.util.DBUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

// Runs once when the app starts and once when it stops
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Application-wide values, readable from any servlet or JSP
        event.getServletContext().setAttribute("appName", "ShopSphere");
        event.getServletContext().setAttribute("activeSessions", new AtomicInteger(0));

        // Check the database once at startup so problems show up immediately
        try (Connection con = DBUtil.getConnection()) {
            System.out.println("[ShopSphere] Started. Database connection OK.");
        } catch (Exception e) {
            System.out.println("[ShopSphere] Started, but database connection FAILED: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("[ShopSphere] Application stopped.");
    }
}