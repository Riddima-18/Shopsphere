package com.shopsphere.network;

import com.shopsphere.model.Product;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class InventoryClient {

    private static final String HOST = "localhost";

    public static void main(String[] args) {
        // Change these IDs to products that exist in your products table
        run("GET_PRODUCT 4");
        run("GET_STOCK 4");
        run("SEARCH_PRODUCT shirt");
        run("GET_STOCK 999");
    }

    private static void run(String request) {
        System.out.println("\n> " + request);
        try {
            show(send(request));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not reach server: " + e.getMessage());
        }
    }

    private static Object send(String request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(HOST, InventoryServer.PORT)) {
            // Send the request first, then read the reply
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        }
    }

    private static void show(Object reply) {
        if (reply instanceof String text) {
            System.out.println(text);
        } else if (reply instanceof Integer stock) {
            System.out.println("Stock: " + stock);
        } else if (reply instanceof Product p) {
            printProduct(p);
        } else if (reply instanceof List<?> list) {
            if (list.isEmpty()) {
                System.out.println("No matching products.");
            }
            for (Object item : list) {
                if (item instanceof Product p) {
                    printProduct(p);
                }
            }
        } else {
            System.out.println("Unexpected reply: " + reply);
        }
    }

    private static void printProduct(Product p) {
        System.out.println("ID: " + p.getProductId()
                + " | " + p.getName()
                + " | Rs. " + String.format("%.2f", p.getFinalPrice())
                + " | Stock: " + p.getStock());
    }
}