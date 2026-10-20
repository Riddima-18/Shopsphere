package com.shopsphere.rmi;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private final ProductDAO productDAO = new ProductDAO();

    public InventoryServiceImpl() throws RemoteException {
        super();   // exports this object so clients can call it
    }

    @Override
    public Product getProduct(int productId) throws RemoteException {
        try {
            return productDAO.findById(productId);
        } catch (SQLException e) {
            throw new RemoteException("Database error", e);
        }
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        Product p = getProduct(productId);
        if (p == null) {
            throw new RemoteException("Product " + productId + " not found");
        }
        return p.getStock();
    }

    // Uses the same stock check as the web app: the update fails if stock would go below zero
    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        Product p = getProduct(productId);
        if (p == null) {
            throw new RemoteException("Product " + productId + " not found");
        }
        int newStock = p.getStock() + quantity;
        if (newStock < 0) {
            return false;
        }
        try {
            p.setStock(newStock);
            productDAO.update(p);
            return true;
        } catch (SQLException e) {
            throw new RemoteException("Database error", e);
        }
    }
}