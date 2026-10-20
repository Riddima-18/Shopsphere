package com.shopsphere.controller;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.service.AdminProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private final AdminProductService productService = new AdminProductService();
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("new".equals(action)) {
                Product blank = new Product();
                blank.setStatus(true);
                showForm(req, resp, blank, null);

            } else if ("edit".equals(action)) {
                Product product = productDAO.findById(parseInt(req.getParameter("id")));
                if (product == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }
                showForm(req, resp, product, null);

            } else {
                req.setAttribute("products", productDAO.findAllForAdmin());
                req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Could not load products", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("toggle".equals(action)) {
                productService.toggleStatus(parseInt(req.getParameter("id")));
                redirectWith(req, resp, "message", "Product status updated.");

            } else if ("save".equals(action)) {
                Product product = readForm(req);
                try {
                    productService.save(product);
                    redirectWith(req, resp, "message", "Product saved.");
                } catch (IllegalArgumentException e) {
                    // Redisplay the form with the user's input and the error
                    showForm(req, resp, product, e.getMessage());
                }

            } else {
                redirectWith(req, resp, "error", "Unknown action.");
            }
        } catch (IllegalArgumentException e) {
            redirectWith(req, resp, "error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Product update failed", e);
        }
    }

    // Builds a Product from the form. Invalid numbers become -1, which validation rejects.
    private Product readForm(HttpServletRequest req) {
        Product p = new Product();
        p.setProductId(parseInt(req.getParameter("productId")));
        p.setCategoryId(parseInt(req.getParameter("categoryId")));
        p.setName(req.getParameter("name"));
        p.setBrand(req.getParameter("brand"));
        p.setDescription(req.getParameter("description"));
        p.setPrice(parseDouble(req.getParameter("price")));
        p.setDiscount(parseDouble(req.getParameter("discount")));
        p.setStock(parseInt(req.getParameter("stock")));
        p.setImageUrl(req.getParameter("imageUrl"));
        p.setStatus("true".equals(req.getParameter("status")));
        return p;
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp,
                          Product product, String error)
            throws ServletException, IOException, SQLException {
        req.setAttribute("product", product);
        req.setAttribute("categories", categoryDAO.findAll());
        req.setAttribute("error", error);
        req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private void redirectWith(HttpServletRequest req, HttpServletResponse resp,
                              String key, String text) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/products?" + key + "="
                + URLEncoder.encode(text, StandardCharsets.UTF_8));
    }
}