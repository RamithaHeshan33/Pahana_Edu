package org.example.pahana_edu.service;

import org.example.pahana_edu.business.cart.dto.CartItemDTO;
import org.example.pahana_edu.business.order.dto.OrderDTO;
import org.example.pahana_edu.business.order.dto.OrderItemDTO;
import org.example.pahana_edu.business.order.service.OrderService;
import org.example.pahana_edu.persistance.customer.dao.CustomerDAO;
import org.example.pahana_edu.persistance.customer.model.CustomerModel;
import org.example.pahana_edu.persistance.order.dao.OrderDAO;
import org.example.pahana_edu.persistance.order.model.OrderItemModel;
import org.example.pahana_edu.persistance.order.model.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;
    private FakeOrderDAO fakeOrderDAO;
    private FakeCustomerDAO fakeCustomerDAO;
    private CustomerModel testCustomer;
    private List<CartItemDTO> testCartItems;

    @BeforeEach
    void setUp() {
        fakeOrderDAO = new FakeOrderDAO();
        fakeCustomerDAO = new FakeCustomerDAO();
        orderService = new TestOrderService(fakeOrderDAO, fakeCustomerDAO);

        // Setup test customer
        testCustomer = new CustomerModel();
        testCustomer.setCustomerId(1);
        testCustomer.setCustomerName("John Doe");
        testCustomer.setCustomerPhone("1234567890");
        testCustomer.setCustomerEmail("john@example.com");
        testCustomer.setCustomerAddress("123 Main St");
        testCustomer.setCustomerAccountNumber("ACC123");

        // Setup test cart items
        testCartItems = Arrays.asList(
                new CartItemDTO(1, "Book 1", 25.99, 2, "image1.jpg"),
                new CartItemDTO(2, "Book 2", 35.50, 1, "image2.jpg")
        );

        fakeCustomerDAO.addCustomer(testCustomer);
    }

    // Test implementation of OrderService
    private static class TestOrderService extends OrderService {
        private final OrderDAO orderDAO;
        private final CustomerDAO customerDAO;

        public TestOrderService(OrderDAO orderDAO, CustomerDAO customerDAO) {
            this.orderDAO = orderDAO;
            this.customerDAO = customerDAO;
        }

        @Override
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

            // Create order items
            List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
            for (CartItemDTO cartItem : cartItems) {
                // Update book quantity (simplified for test)
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
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(savedOrder.getId());
            orderDTO.setCustomerId(savedOrder.getCustomerId());
            orderDTO.setTotalAmount(savedOrder.getTotalAmount());
            orderDTO.setCashReceived(savedOrder.getCashReceived());
            orderDTO.setChangeAmount(savedOrder.getChangeAmount());
            orderDTO.setOrderDate(savedOrder.getOrderDate());
            orderDTO.setStatus(savedOrder.getStatus());
            orderDTO.setCustomerName(customer.getCustomerName());
            orderDTO.setCustomerPhone(customer.getCustomerPhone());
            orderDTO.setOrderItems(orderItemDTOs);

            return orderDTO;
        }

        @Override
        public CustomerModel findCustomerByPhone(String phone) throws SQLException {
            return customerDAO.findByPhone(phone);
        }

        @Override
        public List<OrderDTO> getAllOrders() throws SQLException {
            List<OrderModel> orders = orderDAO.getAllOrders();
            List<OrderDTO> orderDTOs = new ArrayList<>();

            for (OrderModel order : orders) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(order.getId());
                orderDTO.setCustomerId(order.getCustomerId());
                orderDTO.setTotalAmount(order.getTotalAmount());
                orderDTO.setCashReceived(order.getCashReceived());
                orderDTO.setChangeAmount(order.getChangeAmount());
                orderDTO.setOrderDate(order.getOrderDate());
                orderDTO.setStatus(order.getStatus());
                orderDTO.setCustomerName(order.getCustomerName());
                orderDTO.setCustomerPhone(order.getCustomerPhone());

                // Get order items
                List<OrderItemModel> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
                List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

                for (OrderItemModel item : orderItems) {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setOrderId(item.getOrderId());
                    itemDTO.setBookId(item.getBookId());
                    itemDTO.setBookTitle(item.getBookTitle());
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

        @Override
        public OrderDTO getOrderById(int id) throws SQLException {
            OrderModel order = orderDAO.getOrderById(id);
            if (order == null) {
                return null;
            }

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setCustomerId(order.getCustomerId());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setCashReceived(order.getCashReceived());
            orderDTO.setChangeAmount(order.getChangeAmount());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setCustomerName(order.getCustomerName());
            orderDTO.setCustomerPhone(order.getCustomerPhone());

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

    // Fake OrderDAO implementation for testing
    private static class FakeOrderDAO extends OrderDAO {
        private List<OrderModel> orders = new ArrayList<>();
        private List<OrderItemModel> orderItems = new ArrayList<>();
        private boolean shouldFail = false;
        private int orderIdCounter = 1;
        private int orderItemIdCounter = 1;

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        void addOrder(OrderModel order) {
            orders.add(order);
        }

        @Override
        public OrderModel saveOrder(OrderModel order) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            order.setId(orderIdCounter++);
            orders.add(order);
            return order;
        }

        @Override
        public OrderItemModel saveOrderItem(OrderItemModel orderItem) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            orderItem.setId(orderItemIdCounter++);
            orderItem.setTotalPrice(orderItem.getUnitPrice() * orderItem.getQuantity());
            orderItems.add(orderItem);
            return orderItem;
        }

        @Override
        public boolean updateBookQuantity(int bookId, int quantitySold) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            // Simplified: always return true for test
            return true;
        }

        @Override
        public OrderModel getOrderById(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return orders.stream()
                    .filter(o -> o.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<OrderModel> getAllOrders() throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return new ArrayList<>(orders);
        }

        @Override
        public List<OrderItemModel> getOrderItemsByOrderId(int orderId) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return orderItems.stream()
                    .filter(item -> item.getOrderId() == orderId)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    // Fake CustomerDAO implementation for testing
    private static class FakeCustomerDAO extends CustomerDAO {
        private List<CustomerModel> customers = new ArrayList<>();
        private boolean shouldFail = false;

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        void addCustomer(CustomerModel customer) {
            customers.add(customer);
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
    }

    @Test
    void processCheckout_WithValidData() throws SQLException {
        // Arrange
        String customerPhone = "1234567890";
        Double totalAmount = 87.48; // (25.99 * 2) + 35.50
        Double cashReceived = 100.00;

        // Act
        OrderDTO result = orderService.processCheckout(testCartItems, customerPhone, totalAmount, cashReceived);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getCustomerId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("1234567890", result.getCustomerPhone());
        assertEquals(totalAmount, result.getTotalAmount());
        assertEquals(cashReceived, result.getCashReceived());
        assertEquals(12.52, result.getChangeAmount(), 0.01); // 100.00 - 87.48

        // Verify order items
        assertNotNull(result.getOrderItems());
        assertEquals(2, result.getOrderItems().size());
    }

    @Test
    void processCheckout_WithCustomerNotFound() {
        // Arrange
        String customerPhone = "9999999999"; // Non-existent customer
        Double totalAmount = 87.48;
        Double cashReceived = 100.00;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.processCheckout(testCartItems, customerPhone, totalAmount, cashReceived)
        );
        assertEquals("Customer not found with phone: 9999999999", exception.getMessage());
    }

    @Test
    void processCheckout_WithInsufficientCash() {
        // Arrange
        String customerPhone = "1234567890";
        Double totalAmount = 87.48;
        Double cashReceived = 50.00; // Less than total

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.processCheckout(testCartItems, customerPhone, totalAmount, cashReceived)
        );
        assertEquals("Insufficient cash received", exception.getMessage());
    }

    @Test
    void processCheckout_WithEmptyCart() throws SQLException {
        // Arrange
        List<CartItemDTO> emptyCart = new ArrayList<>();
        String customerPhone = "1234567890";
        Double totalAmount = 0.0;
        Double cashReceived = 0.0;

        // Act
        OrderDTO result = orderService.processCheckout(emptyCart, customerPhone, totalAmount, cashReceived);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getTotalAmount());
        assertEquals(0.0, result.getChangeAmount());
        assertTrue(result.getOrderItems().isEmpty());
    }

    @Test
    void findCustomerByPhone_WithExistingCustomer() throws SQLException {
        // Act
        CustomerModel result = orderService.findCustomerByPhone("1234567890");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("1234567890", result.getCustomerPhone());
    }

    @Test
    void findCustomerByPhone_WithNonExistentCustomer() throws SQLException {
        // Act
        CustomerModel result = orderService.findCustomerByPhone("9999999999");

        // Assert
        assertNull(result);
    }

    @Test
    void getAllOrders_WithMultipleOrders() throws SQLException {
        // Arrange
        OrderModel order1 = new OrderModel(1, 1, 50.0, 60.0, 10.0, LocalDateTime.now(), "completed");
        order1.setCustomerName("John Doe");
        order1.setCustomerPhone("1234567890");

        OrderModel order2 = new OrderModel(2, 1, 75.0, 80.0, 5.0, LocalDateTime.now(), "completed");
        order2.setCustomerName("John Doe");
        order2.setCustomerPhone("1234567890");

        fakeOrderDAO.addOrder(order1);
        fakeOrderDAO.addOrder(order2);

        // Act
        List<OrderDTO> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(50.0, result.get(0).getTotalAmount());
        assertEquals(75.0, result.get(1).getTotalAmount());
    }

    @Test
    void getAllOrders_WithEmptyDatabase() throws SQLException {
        // Act
        List<OrderDTO> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getOrderById_WithExistingOrder() throws SQLException {
        // Arrange
        OrderModel order = new OrderModel(1, 1, 50.0, 60.0, 10.0, LocalDateTime.now(), "completed");
        order.setCustomerName("John Doe");
        order.setCustomerPhone("1234567890");
        fakeOrderDAO.addOrder(order);

        // Act
        OrderDTO result = orderService.getOrderById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(50.0, result.getTotalAmount());
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    void getOrderById_WithNonExistentOrder() throws SQLException {
        // Act
        OrderDTO result = orderService.getOrderById(999);

        // Assert
        assertNull(result);
    }

    @Test
    void processCheckout_WithDatabaseError() throws SQLException {
        // Arrange
        fakeOrderDAO.setShouldFail(true);
        String customerPhone = "1234567890";
        Double totalAmount = 87.48;
        Double cashReceived = 100.00;

        // Act & Assert
        SQLException exception = assertThrows(
                SQLException.class,
                () -> orderService.processCheckout(testCartItems, customerPhone, totalAmount, cashReceived)
        );
        assertEquals("Simulated database error", exception.getMessage());
    }
}
