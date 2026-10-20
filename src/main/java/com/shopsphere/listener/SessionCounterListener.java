package com.shopsphere.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

// Counts active sessions. Each visitor gets a session, logged in or not.
@WebListener
public class SessionCounterListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        counter(event).incrementAndGet();
    }

    // Called on logout (invalidate) and when a session times out
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        counter(event).decrementAndGet();
    }

    private AtomicInteger counter(HttpSessionEvent event) {
        return (AtomicInteger) event.getSession().getServletContext().getAttribute("activeSessions");
    }
}