package org.example.pahana_edu.business.order.service;

import org.example.pahana_edu.business.cart.dto.CartItemDTO;
import org.example.pahana_edu.business.order.dto.OrderDTO;
import org.example.pahana_edu.business.order.dto.OrderItemDTO;
import org.example.pahana_edu.business.order.mapper.OrderMapper;
import org.example.pahana_edu.persistance.customer.dao.CustomerDAO;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;
import org.example.pahana_edu.persistance.order.dao.OrderDAO;
import org.example.pahana_edu.persistance.order.model.OrderItemModel;
import org.example.pahana_edu.persistance.order.model.OrderModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final CustomerDAO customerDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.customerDAO = new CustomerDAO();
    }

    public OrderDTO processCheckout(List<CartItemDTO> cartItems, String customerPhone,
                                    Double totalAmount, Double cashReceived) throws SQLException {

        // Find customer by phone
        CustomerModel customer = customerDAO.findByPhone(customerPhone);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with phone: " + customerPhone);
        }

        // Validate cash received
        if (cashReceived < totalAmount) {
            throw new IllegalArgumentException("Insufficient cash received");
        }

        Double changeAmount = cashReceived - totalAmount;

        // Create order
        OrderModel order = new OrderModel(customer.getCustomerId(), totalAmount, cashReceived, changeAmount);
        OrderModel savedOrder = orderDAO.saveOrder(order);

        // Create order items and update book quantities
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (CartItemDTO cartItem : cartItems) {
            // Update book quantity
            boolean quantityUpdated = orderDAO.updateBookQuantity(cartItem.getBookId(), cartItem.getQuantity());
            if (!quantityUpdated) {
                throw new SQLException("Insufficient stock for book: " + cartItem.getTitle());
            }

            // Create order item
            OrderItemModel orderItem = new OrderItemModel(
                    savedOrder.getId(),
                    cartItem.getBookId(),
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            OrderItemModel savedOrderItem = orderDAO.saveOrderItem(orderItem);

            // Convert to DTO
            OrderItemDTO orderItemDTO = new OrderItemDTO(
                    savedOrderItem.getOrderId(),
                    savedOrderItem.getBookId(),
                    cartItem.getTitle(),
                    savedOrderItem.getQuantity(),
                    savedOrderItem.getUnitPrice()
            );
            orderItemDTO.setId(savedOrderItem.getId());
            orderItemDTO.setTotalPrice(savedOrderItem.getTotalPrice());
            orderItemDTOs.add(orderItemDTO);
        }

        // Convert order to DTO
        OrderDTO orderDTO = OrderMapper.toDTO(savedOrder);
        orderDTO.setCustomerName(customer.getCustomerName());
        orderDTO.setCustomerPhone(customer.getCustomerPhone());
        orderDTO.setOrderItems(orderItemDTOs);

        return orderDTO;
    }

    public CustomerModel findCustomerByPhone(String phone) throws SQLException {
        return customerDAO.findByPhone(phone);
    }

    public List<OrderDTO> getAllOrders() throws SQLException {
        List<OrderModel> orders = orderDAO.getAllOrders();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (OrderModel order : orders) {
            OrderDTO orderDTO = OrderMapper.toDTO(order);

            // Get order items
            List<OrderItemModel> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
            List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

            for (OrderItemModel item : orderItems) {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setOrderId(item.getOrderId());
                itemDTO.setBookId(item.getBookId());
                itemDTO.setBookTitle(item.getBookTitle()); // Set the book title from the model
                itemDTO.setBookTitle(item.getBookTitle()); // Set the book title from the model
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setUnitPrice(item.getUnitPrice());
                itemDTO.setTotalPrice(item.getTotalPrice());
                orderItemDTOs.add(itemDTO);
            }

            orderDTO.setOrderItems(orderItemDTOs);
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
    }

    public OrderDTO getOrderById(int id) throws SQLException {
        OrderModel order = orderDAO.getOrderById(id);
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = OrderMapper.toDTO(order);

        // Get order items
        List<OrderItemModel> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

        for (OrderItemModel item : orderItems) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setOrderId(item.getOrderId());
            itemDTO.setBookId(item.getBookId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setTotalPrice(item.getTotalPrice());
            orderItemDTOs.add(itemDTO);
        }

        orderDTO.setOrderItems(orderItemDTOs);
        return orderDTO;
    }
}
