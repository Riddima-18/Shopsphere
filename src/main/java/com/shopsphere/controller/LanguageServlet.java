package com.shopsphere.controller;

import com.shopsphere.util.I18n;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Example: /language?lang=hi, then the browser goes back to the page it came from
@WebServlet("/language")
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().setAttribute(I18n.SESSION_KEY, I18n.parse(req.getParameter("lang")));

        // Only redirect to paths inside this app, to avoid sending users to other sites
        String back = req.getParameter("back");
        if (back == null || !back.startsWith("/")) {
            back = "/products";
        }
        resp.sendRedirect(req.getContextPath() + back);
    }
}
