package org.example.pahana_edu.business.customer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.business.customer.dto.CustomerDTO;
import org.example.pahana_edu.business.customer.service.CustomerService;
import org.example.pahana_edu.persistance.customer.dao.CustomerDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "customerController", urlPatterns = {"/customers/*"})
public class CustomerController extends HttpServlet {
    private CustomerService customerService;

    // Default constructor
    public CustomerController() {
        this.customerService = new CustomerService(new CustomerDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        switch (pathInfo) {
            case "/ManageCustomers":
                handleManageCustomers(request, response);
                break;
            case "/getCustomer":
                handleGetCustomer(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (pathInfo) {
            case "/addCustomer":
                handleAddCustomer(request, response);
                break;
            case "/updateCustomer":
                handleUpdateCustomer(request, response);
                break;
            case "/deleteCustomer":
                handleDeleteCustomer(request, response);
                break;
        }
    }

    private void handleAddCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String customerAddress = request.getParameter("customerAddress");
        String customerAccountNumber = request.getParameter("customerAccountNumber");

        CustomerDTO customerDTO = new CustomerDTO(customerName, customerEmail, customerPhone, customerAddress, customerAccountNumber);

        if (!customerDTO.isValid()) {
            request.setAttribute("customerDTO", customerDTO);
            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
            return;
        }

        CustomerDTO addedCustomer = customerService.saveCustomer(request, response);
        if (addedCustomer != null) {
            response.sendRedirect(request.getContextPath() + "/customers/ManageCustomers");
        }
    }

    private void handleManageCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //get all customers
            List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
            request.setAttribute("customerDTOs", customerDTOs);

            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
        }
        catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
        }
    }

    private void handleGetCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerIdStr = request.getParameter("id");
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdStr);
            CustomerDTO customer = customerService.getCustomerById(customerId);

            if (customer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                return;
            }

            // Return JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format(
                    "{\"id\":%d,\"customerName\":\"%s\",\"customerEmail\":\"%s\",\"customerPhone\":\"%s\",\"customerAddress\":\"%s\",\"customerAccountNumber\":\"%s\"}",
                    customer.getId(),
                    escapeJson(customer.getCustomerName()),
                    escapeJson(customer.getCustomerEmail()),
                    escapeJson(customer.getCustomerPhone() != null ? customer.getCustomerPhone() : ""),
                    escapeJson(customer.getCustomerAddress() != null ? customer.getCustomerAddress() : ""),
                    escapeJson(customer.getCustomerAccountNumber() != null ? customer.getCustomerAccountNumber() : "")
            ));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void handleUpdateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerDTO updatedCustomer = customerService.updateCustomer(request, response);
        if (updatedCustomer != null) {
            response.sendRedirect(request.getContextPath() + "/customers/ManageCustomers");
        }
    }

    private void handleDeleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerIdStr = request.getParameter("customerId");
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdStr);
            boolean deleted = customerService.deleteCustomer(customerId, request);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/customers/ManageCustomers");
            } else {
                request.setAttribute("error", "Failed to delete customer");
                handleManageCustomers(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            handleManageCustomers(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Database error occurred while deleting customer");
            handleManageCustomers(request, response);
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
