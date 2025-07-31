package org.example.pahana_edu.business.cart.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.pahana_edu.business.cart.dto.CartItemDTO;
import org.example.pahana_edu.business.order.dto.OrderDTO;
import org.example.pahana_edu.business.order.service.OrderService;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "checkoutController", urlPatterns = {"/checkout/*"})
public class CheckoutController extends HttpServlet {

    private final OrderService orderService;

    public CheckoutController() {
        this.orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        switch (pathInfo) {
            case "/verify-customer":
                handleVerifyCustomer(request, response);
                break;
            case "/process":
                handleProcessCheckout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleVerifyCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String phone = request.getParameter("phone");

        if (phone == null || phone.trim().isEmpty()) {
            sendJsonResponse(response, false, "Phone number is required", null);
            return;
        }

        try {
            CustomerModel customer = orderService.findCustomerByPhone(phone.trim());

            if (customer != null) {
                sendJsonResponse(response, true, "Customer found",
                        "{\"id\": " + customer.getCustomerId() +
                                ", \"name\": \"" + escapeJson(customer.getCustomerName()) +
                                "\", \"phone\": \"" + escapeJson(customer.getCustomerPhone()) + "\"}");
            } else {
                sendJsonResponse(response, false, "Customer not found in database", null);
            }

        } catch (SQLException e) {
            sendJsonResponse(response, false, "Database error occurred", null);
        }
    }

    private void handleProcessCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String customerPhone = request.getParameter("customerPhone");
            String totalAmountStr = request.getParameter("totalAmount");
            String cashReceivedStr = request.getParameter("cashReceived");

            if (customerPhone == null || totalAmountStr == null || cashReceivedStr == null) {
                sendJsonResponse(response, false, "Missing required parameters", null);
                return;
            }

            Double totalAmount = Double.parseDouble(totalAmountStr);
            Double cashReceived = Double.parseDouble(cashReceivedStr);

            // Get cart from session
            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                sendJsonResponse(response, false, "Cart is empty", null);
                return;
            }

            // Process checkout
            OrderDTO order = orderService.processCheckout(cart, customerPhone, totalAmount, cashReceived);

            // Clear cart after successful checkout
            session.removeAttribute("cart");

            // Send success response with order details
            String orderJson = "{\"orderId\": " + order.getId() +
                    ", \"customerName\": \"" + escapeJson(order.getCustomerName()) +
                    "\", \"totalAmount\": " + order.getTotalAmount() +
                    ", \"cashReceived\": " + order.getCashReceived() +
                    ", \"changeAmount\": " + order.getChangeAmount() + "}";

            sendJsonResponse(response, true, "Checkout completed successfully", orderJson);

        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid number format", null);
        } catch (IllegalArgumentException e) {
            sendJsonResponse(response, false, e.getMessage(), null);
        } catch (SQLException e) {
            sendJsonResponse(response, false, "Database error occurred during checkout", null);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message, String data)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"success\": ").append(success)
                .append(", \"message\": \"").append(escapeJson(message)).append("\"");

        if (data != null) {
            json.append(", \"data\": ").append(data);
        }

        json.append("}");
        out.print(json.toString());
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
