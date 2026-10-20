package com.shopsphere.jndi;

import com.shopsphere.util.DBUtil;
import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public class JndiDemo {

    public static void main(String[] args) {
        System.out.println("JNDI lookup demo");
        System.out.println("----------------");

        try {
            // InitialContext is the entry point to any naming service
            Context ctx = new InitialContext();
            System.out.println("Initial context created: " + ctx.getClass().getName());

            // In Tomcat, a DataSource is registered under java:comp/env/... in context.xml.
            // This name is not registered here, so the lookup shows JNDI's error.
            try {
                ctx.lookup("java:comp/env/jdbc/shopsphere");
            } catch (NameNotFoundException e) {
                System.out.println("Lookup failed as expected: name not bound (" + e.getExplanation() + ")");
            }

            ctx.close();
        } catch (NamingException e) {
            // Without a JNDI provider, InitialContext itself may fail. This is also useful to explain.
            System.out.println("No JNDI provider configured: " + e.getMessage());
        }

        System.out.println("\nDirect JDBC connection the app uses today:");
        try (Connection con = DBUtil.getConnection()) {
            System.out.println("Connected to: " + con.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}