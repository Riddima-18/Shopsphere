package com.shopsphere.rmi;

import com.shopsphere.model.Product;
import java.rmi.Remote;
import java.rmi.RemoteException;

// The contract shared by the server and the client
public interface InventoryService extends Remote {
    Product getProduct(int productId) throws RemoteException;
    int getStock(int productId) throws RemoteException;
    boolean updateStock(int productId, int quantity) throws RemoteException;
}