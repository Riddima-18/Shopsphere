package com.shopsphere.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.util.RecentCookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String keyword = req.getParameter("q");
        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Product product = productDAO.findById(id);
                if (product == null || !product.isStatus()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }
                // Record this view in the cookie. Cookies must be added before forwarding.
                List<Integer> recent = RecentCookie.read(req);
                RecentCookie.remember(recent, id);
                RecentCookie.write(resp, req.getContextPath(), recent);

                req.setAttribute("product", product);
                req.getRequestDispatcher("/product-details.jsp").forward(req, resp);
                return;
            }

            List<Product> products = (keyword == null || keyword.isBlank())
                    ? productDAO.findAll()
                    : productDAO.search(keyword.trim());
            req.setAttribute("products", products);
            req.setAttribute("keyword", keyword);
            req.setAttribute("recentProducts", loadRecent(RecentCookie.read(req)));
            req.getRequestDispatcher("/products.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product id");
        } catch (SQLException e) {
            throw new ServletException("Could not load products", e);
        }
    }

    // Loads the recently viewed products, skipping any that were disabled
    private List<Product> loadRecent(List<Integer> ids) throws SQLException {
        List<Product> list = new ArrayList<>();
        for (int id : ids) {
            Product p = productDAO.findById(id);
            if (p != null && p.isStatus()) {
                list.add(p);
            }
        }
        return list;
    }
}