package com.shopsphere.network;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Protocol: the client sends one text line such as "GET_STOCK 4".
// The server replies with one serialized Java object:
//   Product, Integer, List<Product>, or a String starting with "ERROR:".
public class InventoryServer {

    public static final int PORT = 5000;
    private static final ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) throws IOException {
        System.out.println("Inventory server listening on port " + PORT);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                // One request at a time. Each connection handles one command and closes.
                try (Socket client = server.accept()) {
                    handle(client);
                } catch (IOException e) {
                    System.out.println("Connection error: " + e.getMessage());
                }
            }
        }
    }

    private static void handle(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String request = in.readLine();
        System.out.println("Request: " + request);

        Object response = process(request);

        // Create the object stream after reading the request, so the client can
        // send its request before waiting for the reply
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(response);
        out.flush();
    }

    private static Object process(String request) {
        if (request == null || request.isBlank()) {
            return "ERROR: empty request";
        }
        String[] parts = request.trim().split("\\s+", 2);
        String command = parts[0].toUpperCase();
        String argument = parts.length > 1 ? parts[1].trim() : "";

        try {
            if (command.equals("GET_PRODUCT")) {
                Product p = productDAO.findById(Integer.parseInt(argument));
                if (p == null || !p.isStatus()) {
                    return "ERROR: product not found";
                }
                return p;
            }
            if (command.equals("GET_STOCK")) {
                Product p = productDAO.findById(Integer.parseInt(argument));
                if (p == null) {
                    return "ERROR: product not found";
                }
                return Integer.valueOf(p.getStock());
            }
            if (command.equals("SEARCH_PRODUCT")) {
                // Only active products are returned to customers
                List<Product> matches = new ArrayList<>();
                for (Product p : productDAO.search(argument)) {
                    matches.add(p);
                }
                return matches;
            }
            return "ERROR: unknown command " + command;
        } catch (NumberFormatException e) {
            return "ERROR: product id must be a number";
        } catch (SQLException e) {
            return "ERROR: database problem";
        }
    }
}