package org.example.pahana_edu.business.order.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.pahana_edu.business.order.dto.OrderDTO;
import org.example.pahana_edu.business.order.service.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "orderController", urlPatterns = {"/orders/*"})
public class OrderController extends HttpServlet {

    private final OrderService orderService;

    public OrderController() {
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        switch (pathInfo) {
            case "/ManageOrders":
                handleManageOrders(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleManageOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/Admin/ManageOrders.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading orders: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageOrders.jsp").forward(request, response);
        }
    }
}
