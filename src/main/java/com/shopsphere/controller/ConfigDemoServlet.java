package com.shopsphere.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
    value = "/config-demo",
    initParams = {
        @WebInitParam(name = "greeting", value = "Hello from ServletConfig"),
        @WebInitParam(name = "pageSize", value = "12")
    })
public class ConfigDemoServlet extends HttpServlet {

    private String greeting;
    private int pageSize;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);   // keeps getServletConfig() working
        greeting = config.getInitParameter("greeting");
        pageSize = Integer.parseInt(config.getInitParameter("pageSize"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<h2>ServletConfig and ServletContext</h2>");
        out.println("<table border='1' cellpadding='6'>");
        out.println("<tr><td>Servlet name</td><td>" + getServletName() + "</td></tr>");
        out.println("<tr><td>Init param: greeting</td><td>" + greeting + "</td></tr>");
        out.println("<tr><td>Init param: pageSize</td><td>" + pageSize + "</td></tr>");
        out.println("<tr><td>App name (ServletContext, set by AppListener)</td><td>"
                + getServletContext().getAttribute("appName") + "</td></tr>");
        out.println("<tr><td>Context path</td><td>" + req.getContextPath() + "</td></tr>");
        out.println("<tr><td>Server</td><td>" + getServletContext().getServerInfo() + "</td></tr>");
        out.println("</table>");
    }
}