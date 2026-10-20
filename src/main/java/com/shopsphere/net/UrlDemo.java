package com.shopsphere.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class UrlDemo {

    public static void main(String[] args) throws Exception {
        // Parse the address into its parts
        URL url = new URL("http://localhost:8080/shopsphere/products");
        System.out.println("Protocol: " + url.getProtocol());
        System.out.println("Host:     " + url.getHost());
        System.out.println("Port:     " + url.getPort());
        System.out.println("Path:     " + url.getPath());

        // Open a connection and read the response headers
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();

        System.out.println("\nResponse headers:");
        for (Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                System.out.println("  " + header.getKey() + ": " + header.getValue());
            }
        }

        System.out.println("\nContent type: " + conn.getContentType());

        // Read the first few lines of the body
        System.out.println("\nFirst lines of the page:");
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            int count = 0;
            while ((line = in.readLine()) != null && count < 5) {
                System.out.println("  " + line);
                count++;
            }
        }
    }
}