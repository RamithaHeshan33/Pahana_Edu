package org.example.pahana_edu.business.customer.dto;

public class CustomerDTO {
    private Integer id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String customerAccountNumber;

    public CustomerDTO(String customerName, String customerEmail, String customerPhone, String customerAddress,
                       String customerAccountNumber) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerAccountNumber = customerAccountNumber;
    }

    public CustomerDTO(Integer id, String customerName, String customerEmail, String customerPhone, String customerAddress,
                       String customerAccountNumber) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerAccountNumber = customerAccountNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public boolean isValid() {
        return customerName != null && !customerName.trim().isEmpty()
                && customerEmail != null && !customerEmail.trim().isEmpty()
                && customerPhone != null && !customerPhone.trim().isEmpty()
                && customerAddress != null && !customerAddress.trim().isEmpty()
                && customerAccountNumber != null && !customerAccountNumber.trim().isEmpty();
    }
}
