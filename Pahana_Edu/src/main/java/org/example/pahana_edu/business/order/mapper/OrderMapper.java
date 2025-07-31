package org.example.pahana_edu.business.order.mapper;

import org.example.pahana_edu.business.order.dto.OrderDTO;
import org.example.pahana_edu.business.order.dto.OrderItemDTO;
import org.example.pahana_edu.persistance.order.model.OrderItemModel;
import org.example.pahana_edu.persistance.order.model.OrderModel;

public class OrderMapper {

    public static OrderModel toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        return new OrderModel(
                orderDTO.getId(),
                orderDTO.getCustomerId(),
                orderDTO.getTotalAmount(),
                orderDTO.getCashReceived(),
                orderDTO.getChangeAmount(),
                orderDTO.getOrderDate(),
                orderDTO.getStatus()
        );
    }

    public static OrderDTO toDTO(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderModel.getId());
        orderDTO.setCustomerId(orderModel.getCustomerId());
        orderDTO.setTotalAmount(orderModel.getTotalAmount());
        orderDTO.setCashReceived(orderModel.getCashReceived());
        orderDTO.setChangeAmount(orderModel.getChangeAmount());
        orderDTO.setOrderDate(orderModel.getOrderDate());
        orderDTO.setStatus(orderModel.getStatus());
        orderDTO.setCustomerName(orderModel.getCustomerName());
        orderDTO.setCustomerPhone(orderModel.getCustomerPhone());

        return orderDTO;
    }

    public static OrderItemModel toEntity(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null) {
            return null;
        }

        return new OrderItemModel(
                orderItemDTO.getId(),
                orderItemDTO.getOrderId(),
                orderItemDTO.getBookId(),
                orderItemDTO.getBookTitle(),
                orderItemDTO.getQuantity(),
                orderItemDTO.getUnitPrice(),
                orderItemDTO.getTotalPrice()
        );
    }

    public static OrderItemDTO toDTO(OrderItemModel orderItemModel) {
        if (orderItemModel == null) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItemModel.getId());
        orderItemDTO.setOrderId(orderItemModel.getOrderId());
        orderItemDTO.setBookId(orderItemModel.getBookId());
        orderItemDTO.setQuantity(orderItemModel.getQuantity());
        orderItemDTO.setUnitPrice(orderItemModel.getUnitPrice());
        orderItemDTO.setTotalPrice(orderItemModel.getTotalPrice());
        orderItemDTO.setBookTitle(orderItemModel.getBookTitle());

        return orderItemDTO;
    }
}
