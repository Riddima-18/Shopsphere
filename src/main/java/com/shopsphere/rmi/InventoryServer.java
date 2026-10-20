package com.shopsphere.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventoryServer {

    public static void main(String[] args) throws Exception {
        // Start the RMI registry on port 1099
        Registry registry = LocateRegistry.createRegistry(1099);

        // Register the service under a name the client can look up
        registry.rebind("InventoryService", new InventoryServiceImpl());

        System.out.println("RMI inventory service is running on port 1099.");
    }
}