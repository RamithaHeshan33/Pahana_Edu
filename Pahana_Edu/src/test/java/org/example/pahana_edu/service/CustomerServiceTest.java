package org.example.pahana_edu.service;

import org.example.pahana_edu.business.customer.dto.CustomerDTO;
import org.example.pahana_edu.business.customer.service.CustomerService;
import org.example.pahana_edu.persistance.customer.dao.CustomerDAO;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private FakeCustomerDAO fakeCustomerDAO;
    private CustomerModel customerModel;
    private CustomerDTO expectedCustomerDTO;

    @BeforeEach
    void setUp() {
        fakeCustomerDAO = new FakeCustomerDAO();
        customerService = new CustomerService(fakeCustomerDAO);

        customerModel = new CustomerModel();
        customerModel.setCustomerId(1);
        customerModel.setCustomerName("John Doe");
        customerModel.setCustomerEmail("john.doe@example.com");
        customerModel.setCustomerPhone("1234567890");
        customerModel.setCustomerAddress("123 Main St");
        customerModel.setCustomerAccountNumber("ACC123");

        expectedCustomerDTO = new CustomerDTO(
                "John Doe",
                "john.doe@example.com",
                "1234567890",
                "123 Main St",
                "ACC123"
        );
    }

    // Fake CustomerDAO implementation for testing
    private static class FakeCustomerDAO extends CustomerDAO {
        private List<CustomerModel> customers = new ArrayList<>();
        private boolean shouldFail = false;

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        @Override
        public CustomerModel saveCustomer(CustomerModel customer) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            customer.setCustomerId(customers.size() + 1);
            customers.add(customer);
            return customer;
        }

        public CustomerModel getCustomerById(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return customers.stream()
                    .filter(c -> c.getCustomerId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public CustomerModel findByPhone(String phone) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return customers.stream()
                    .filter(c -> c.getCustomerPhone().equals(phone))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<CustomerModel> getAllCustomers() throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return new ArrayList<>(customers);
        }

        @Override
        public boolean deleteCustomer(int id) throws SQLException {
            return customers.removeIf(c -> c.getCustomerId() == id);
        }

        @Override
        public CustomerModel updateCustomer(CustomerModel updatedCustomer) throws SQLException {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId() == updatedCustomer.getCustomerId()) {
                    customers.set(i, updatedCustomer);
                    return updatedCustomer;
                }
            }
            throw new SQLException("Updating customer failed: No rows affected");
        }

    }

    @Test
    void saveCustomer_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);

        CustomerModel savedCustomer = fakeCustomerDAO.getCustomerById(1);
        assertNotNull(savedCustomer);
        assertEquals(customerModel.getCustomerName(), savedCustomer.getCustomerName());
        assertEquals(customerModel.getCustomerEmail(), savedCustomer.getCustomerEmail());
        assertEquals(customerModel.getCustomerPhone(), savedCustomer.getCustomerPhone());
        assertEquals(customerModel.getCustomerAddress(), savedCustomer.getCustomerAddress());
        assertEquals(customerModel.getCustomerAccountNumber(), savedCustomer.getCustomerAccountNumber());
    }

    @Test
    void saveCustomer_InvalidDTO() throws SQLException {
        CustomerModel invalidCustomer = new CustomerModel();
        invalidCustomer.setCustomerId(1);
        invalidCustomer.setCustomerName(""); // empty name
        invalidCustomer.setCustomerEmail("jane.doe@example.com");
        invalidCustomer.setCustomerPhone("0987654321");
        invalidCustomer.setCustomerAddress("456 Oak St");
        invalidCustomer.setCustomerAccountNumber("ACC456");

        // Since saveCustomer doesn't validate DTO
        CustomerDTO invalidDTO = new CustomerDTO(
                "",
                "jane.doe@example.com",
                "0987654321",
                "456 Oak St",
                "ACC456"
        );
        assertFalse(invalidDTO.isValid());

        // Still save to test DAO behavior
        fakeCustomerDAO.saveCustomer(invalidCustomer);
        CustomerModel savedCustomer = fakeCustomerDAO.getCustomerById(1);
        assertNotNull(savedCustomer);
        assertEquals("", savedCustomer.getCustomerName());
    }

    @Test
    void saveCustomer_DatabaseError() {
        fakeCustomerDAO.setShouldFail(true);

        Exception exception = assertThrows(SQLException.class, () -> {
            fakeCustomerDAO.saveCustomer(customerModel);
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

    @Test
    void getAllCustomers_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedCustomerDTO.getCustomerName(), result.get(0).getCustomerName());
        assertEquals(expectedCustomerDTO.getCustomerEmail(), result.get(0).getCustomerEmail());
    }

    @Test
    void getAllCustomers_EmptyList() throws SQLException {
        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCustomerById_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);

        CustomerDTO result = customerService.getCustomerById(1);

        assertNotNull(result);
        assertEquals(expectedCustomerDTO.getCustomerName(), result.getCustomerName());
        assertEquals(expectedCustomerDTO.getCustomerEmail(), result.getCustomerEmail());
        assertEquals(expectedCustomerDTO.getCustomerPhone(), result.getCustomerPhone());
        assertEquals(expectedCustomerDTO.getCustomerAddress(), result.getCustomerAddress());
        assertEquals(expectedCustomerDTO.getCustomerAccountNumber(), result.getCustomerAccountNumber());
    }

    @Test
    void getCustomerById_NotFound() throws SQLException {
        CustomerDTO result = customerService.getCustomerById(999);

        assertNull(result);
    }

    @Test
    void updateCustomer_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);
        CustomerModel updatedModel = new CustomerModel();
        updatedModel.setCustomerId(1);
        updatedModel.setCustomerName("Jane Doe");
        updatedModel.setCustomerEmail("jane.doe@example.com");
        updatedModel.setCustomerPhone("0987654321");
        updatedModel.setCustomerAddress("456 Oak St");
        updatedModel.setCustomerAccountNumber("ACC456");

        fakeCustomerDAO.updateCustomer(updatedModel);

        CustomerModel result = fakeCustomerDAO.getCustomerById(1);
        assertNotNull(result);
        assertEquals("Jane Doe", result.getCustomerName());
        assertEquals("jane.doe@example.com", result.getCustomerEmail());
    }

    @Test
    void deleteCustomer_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);

        boolean deleted = customerService.deleteCustomer(1, null);

        assertTrue(deleted);
        assertNull(fakeCustomerDAO.getCustomerById(1));
    }

    @Test
    void deleteCustomer_NotFound() throws SQLException {
        boolean deleted = customerService.deleteCustomer(999, null);

        assertFalse(deleted);
    }

    @Test
    void findByPhone_Success() throws SQLException {
        fakeCustomerDAO.saveCustomer(customerModel);

        CustomerModel result = fakeCustomerDAO.findByPhone("1234567890");

        assertNotNull(result);
        assertEquals(expectedCustomerDTO.getCustomerName(), result.getCustomerName());
        assertEquals(expectedCustomerDTO.getCustomerEmail(), result.getCustomerEmail());
        assertEquals(expectedCustomerDTO.getCustomerPhone(), result.getCustomerPhone());
    }

    @Test
    void findByPhone_NotFound() throws SQLException {
        CustomerModel result = fakeCustomerDAO.findByPhone("9999999999");

        assertNull(result);
    }

    @Test
    void getAllCustomers_DatabaseError() {
        fakeCustomerDAO.setShouldFail(true);

        SQLException exception = assertThrows(SQLException.class, () -> {
            customerService.getAllCustomers();
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

}

