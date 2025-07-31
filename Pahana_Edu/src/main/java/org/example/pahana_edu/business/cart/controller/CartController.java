package org.example.pahana_edu.business.cart.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.pahana_edu.business.cart.dto.CartItemDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "cartController", urlPatterns = {"/cart/*"})
public class CartController extends HttpServlet {

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
            case "/add":
                handleAddToCart(request, response);
                break;
            case "/remove":
                handleRemoveFromCart(request, response);
                break;
            case "/update":
                handleUpdateCart(request, response);
                break;
            case "/clear":
                handleClearCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
            case "/get":
                handleGetCart(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            String title = request.getParameter("title");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String image = request.getParameter("image");

            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }

            // Check if item already exists in cart
            boolean found = false;
            for (CartItemDTO item : cart) {
                if (item.getBookId() == bookId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }

            if (!found) {
                CartItemDTO newItem = new CartItemDTO(bookId, title, price, quantity, image);
                cart.add(newItem);
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true, \"message\": \"Item added to cart\"}");

        } catch (Exception e) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Error adding item to cart\"}");
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));

            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

            if (cart != null) {
                cart.removeIf(item -> item.getBookId() == bookId);
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true, \"message\": \"Item removed from cart\"}");

        } catch (Exception e) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Error removing item from cart\"}");
        }
    }

    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

            if (cart != null) {
                for (CartItemDTO item : cart) {
                    if (item.getBookId() == bookId) {
                        if (quantity <= 0) {
                            cart.remove(item);
                        } else {
                            item.setQuantity(quantity);
                        }
                        break;
                    }
                }
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true, \"message\": \"Cart updated\"}");

        } catch (Exception e) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Error updating cart\"}");
        }
    }

    private void handleClearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("cart");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"message\": \"Cart cleared\"}");
    }

    private void handleGetCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder json = new StringBuilder();
        json.append("{\"items\": [");

        for (int i = 0; i < cart.size(); i++) {
            CartItemDTO item = cart.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                    .append("\"bookId\": ").append(item.getBookId()).append(",")
                    .append("\"title\": \"").append(item.getTitle().replace("\"", "\\\"")).append("\",")
                    .append("\"price\": ").append(item.getPrice()).append(",")
                    .append("\"quantity\": ").append(item.getQuantity()).append(",")
                    .append("\"image\": \"").append(item.getImage() != null ? item.getImage() : "").append("\"")
                    .append("}");
        }

        json.append("]}");
        out.print(json.toString());
    }
}
