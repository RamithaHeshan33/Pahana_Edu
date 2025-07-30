package org.example.pahana_edu.business.customer.mapper;

import org.example.pahana_edu.business.customer.dto.CustomerDTO;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;

public class CustomerMapper {
    public static CustomerModel toEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }
        return new CustomerModel(customerDTO.getCustomerName(), customerDTO.getCustomerEmail(),
                customerDTO.getCustomerPhone(), customerDTO.getCustomerAddress(), customerDTO.getCustomerAccountNumber());
    }

    public static CustomerDTO toDTO(CustomerModel customerModel) {
        if (customerModel == null) {
            return null;
        }
        return new CustomerDTO(
                customerModel.getCustomerId(),
                customerModel.getCustomerName(),
                customerModel.getCustomerEmail(),
                customerModel.getCustomerPhone(),
                customerModel.getCustomerAddress(),
                customerModel.getCustomerAccountNumber()
        );
    }
}
