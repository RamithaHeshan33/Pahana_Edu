package org.example.pahana_edu.business.customer.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.business.customer.dto.CustomerDTO;
import org.example.pahana_edu.business.customer.mapper.CustomerMapper;
import org.example.pahana_edu.persistance.customer.dao.CustomerDAO;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<CustomerDTO> getAllCustomers() throws SQLException {
        List<CustomerModel> customers = customerDAO.getAllCustomers();
        return customers.stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());

    }

    public CustomerDTO getCustomerById(int id) throws SQLException {
        CustomerModel customer = customerDAO.getCustomerById(id);
        return customer != null ? CustomerMapper.toDTO(customer) : null;
    }

    public CustomerDTO saveCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerName = null;
        String customerEmail = null;
        String customerPhone = null;
        String customerAddress = null;
        String customerAccountNumber = null;

        try {
            customerName = request.getParameter("customerName");
            customerEmail = request.getParameter("customerEmail");
            customerPhone = request.getParameter("customerPhone");
            customerAddress = request.getParameter("customerAddress");
            customerAccountNumber = request.getParameter("customerAccountNumber");

            CustomerDTO dto = new CustomerDTO(customerName, customerEmail, customerPhone, customerAddress, customerAccountNumber);
            if(!dto.isValid()) {
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
                return null;
            }

            CustomerModel customer = CustomerMapper.toEntity(dto);

            CustomerModel addedCustomer = customerDAO.saveCustomer(customer);

            request.getSession().setAttribute("success", "Customer '" + customerName + "' has been added successfully!");

            return new CustomerDTO(
                    addedCustomer.getCustomerName(),
                    addedCustomer.getCustomerEmail(),
                    addedCustomer.getCustomerPhone(),
                    addedCustomer.getCustomerAddress(),
                    addedCustomer.getCustomerAccountNumber()
            );
        } catch (SQLException ex) {
            request.setAttribute("customerName", customerName);
            request.setAttribute("customerEmail", customerEmail);
            request.setAttribute("customerPhone", customerPhone);
            request.setAttribute("customerAddress", customerAddress);
            request.setAttribute("customerAccountNumber", customerAccountNumber);
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
            return null;
        }
    }

    public CustomerDTO updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerIdStr = null;
        String customerName = null;
        String customerEmail = null;
        String customerPhone = null;
        String customerAddress = null;
        String customerAccountNumber = null;

        try {
            customerIdStr = request.getParameter("customerId");
            customerName = request.getParameter("customerName");
            customerEmail = request.getParameter("customerEmail");
            customerPhone = request.getParameter("customerPhone");
            customerAddress = request.getParameter("customerAddress");
            customerAccountNumber = request.getParameter("customerAccountNumber");

            if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Customer ID is required");
                request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
                return null;
            }

            int customerId = Integer.parseInt(customerIdStr);

            CustomerDTO dto = new CustomerDTO(customerId, customerName, customerEmail, customerPhone, customerAddress, customerAccountNumber);
            if (!dto.isValid()) {
                request.setAttribute("error", "All required fields must be filled");
                request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
                return null;
            }

            CustomerModel customer = CustomerMapper.toEntity(dto);
            customer.setCustomerId(customerId);

            CustomerModel updatedCustomer = customerDAO.updateCustomer(customer);

            request.getSession().setAttribute("success", "Customer '" + customerName + "' has been updated successfully!");

            return CustomerMapper.toDTO(updatedCustomer);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
            return null;
        } catch (SQLException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/Admin/ManageCustomers.jsp").forward(request, response);
            return null;
        }
    }

    public boolean deleteCustomer(int id, HttpServletRequest request) throws SQLException {
        boolean deleted = customerDAO.deleteCustomer(id);
        if (deleted && request != null) {
            request.getSession().setAttribute("success", "Customer has been deleted successfully!");
        }
        return deleted;
    }
}
