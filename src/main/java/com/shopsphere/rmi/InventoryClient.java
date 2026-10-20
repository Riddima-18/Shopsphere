package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventoryClient {

    public static void main(String[] args) throws Exception {
        // Connect to the registry on this machine
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);

        // Look up the service by the name the server registered
        InventoryService service = (InventoryService) registry.lookup("InventoryService");

        Product p = service.getProduct(4);
        System.out.println("Product: " + p.getName() + " | Stock: " + p.getStock());

        System.out.println("Stock for product 4: " + service.getStock(4));

        // Remove 2 units, then check the result
        boolean ok = service.updateStock(4, -2);
        System.out.println("Removed 2 units: " + ok + " | New stock: " + service.getStock(4));

        // Try to remove more than is in stock
        boolean tooMany = service.updateStock(4, -100000);
        System.out.println("Removing 100000 units allowed: " + tooMany);

        // Put the 2 units back so the test leaves the data unchanged
        service.updateStock(4, 2);
        System.out.println("Restored stock: " + service.getStock(4));
    }
}