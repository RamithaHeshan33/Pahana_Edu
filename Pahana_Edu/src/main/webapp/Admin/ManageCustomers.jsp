<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/30/2025
  Time: 8:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.pahana_edu.business.user.dto.UserResponseDTO" %>
<%@ page import="org.example.pahana_edu.business.customer.dto.CustomerDTO" %>
<%@ page import="java.util.List" %>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    List<CustomerDTO> customers = (List<CustomerDTO>) request.getAttribute("customers");
    if (customers == null) {
        customers = (List<CustomerDTO>) request.getAttribute("customerDTOs");
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gray-50 min-h-screen">
<div class="flex">
    <!-- Include Sidebar -->
    <%@ include file="../sidebar.jsp" %>

    <!-- Main Content Wrapper -->
    <div class="flex-1 ml-64">
        <!-- Navigation -->
        <nav class="bg-white shadow-sm border-b">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between items-center h-16">
                    <div class="flex items-center">
                        <h1 class="text-xl font-bold text-gray-900">Customers</h1>
                    </div>
                    <div class="flex items-center space-x-4">
                        <div class="flex items-center text-sm text-gray-700">
                            <i class="fas fa-user-circle text-lg mr-2"></i>
                            <span>Welcome, <%= user.getFirstName() %> <%= user.getLastName() %></span>
                        </div>
                    </div>
                </div>
            </div>
        </nav>

        <!-- Main Content -->
        <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
            <!-- Alert Messages -->
            <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                <div class="flex items-center">
                    <i class="fas fa-exclamation-circle mr-2"></i>
                    <%= request.getAttribute("error") %>
                </div>
            </div>
            <% } %>

            <%
                String successMessage = (String) session.getAttribute("success");
                if (successMessage != null) {
            %>
            <div class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-6">
                <div class="flex items-center">
                    <i class="fas fa-check-circle mr-2"></i>
                    <%= successMessage %>
                </div>
            </div>
            <%
                    session.removeAttribute("success");
                }
            %>

            <!-- Add Customer Form -->
            <div class="bg-white rounded-xl shadow-sm p-6 mb-8">
                <h2 class="text-lg font-semibold text-gray-900 mb-4">
                    <i class="fas fa-plus mr-2 text-indigo-600"></i>Add New Customer
                </h2>
                <form id="customerForm" action="${pageContext.request.contextPath}/customers/addCustomer" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label for="customerName" class="block text-sm font-medium text-gray-700 mb-1">
                                Customer Name <span class="text-red-500">*</span>
                            </label>
                            <input type="text" id="customerName" name="customerName" required
                                   value="<%= request.getAttribute("customerName") != null ? request.getAttribute("customerName") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Customer Name" />
                        </div>
                        <div>
                            <label for="customerEmail" class="block text-sm font-medium text-gray-700 mb-1">
                                Email <span class="text-red-500">*</span>
                            </label>
                            <input type="email" id="customerEmail" name="customerEmail" required
                                   value="<%= request.getAttribute("customerEmail") != null ? request.getAttribute("customerEmail") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Customer Email" />
                        </div>
                        <div>
                            <label for="customerPhone" class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
                            <input type="text" id="customerPhone" name="customerPhone"
                                   value="<%= request.getAttribute("customerPhone") != null ? request.getAttribute("customerPhone") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Customer Phone" />
                        </div>
                        <div>
                            <label for="customerAddress" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
                            <input type="text" id="customerAddress" name="customerAddress"
                                   value="<%= request.getAttribute("customerAddress") != null ? request.getAttribute("customerAddress") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Customer Address" />
                        </div>
                        <div>
                            <label for="customerAccountNumber" class="block text-sm font-medium text-gray-700 mb-1">Account Number</label>
                            <input type="text" id="customerAccountNumber" name="customerAccountNumber"
                                   value="<%= request.getAttribute("customerAccountNumber") != null ? request.getAttribute("customerAccountNumber") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Customer Account Number" />
                        </div>
                    </div>
                    <div class="mt-4 flex justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('customerForm').reset()"
                                class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                            Reset
                        </button>
                        <button type="submit"
                                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                            <i class="fas fa-plus mr-1"></i>Add Customer
                        </button>
                    </div>
                </form>
            </div>

            <!-- Customers Table -->
            <div class="bg-white rounded-xl shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-gray-200">
                    <h2 class="text-lg font-semibold text-gray-900">
                        <i class="fas fa-list mr-2 text-indigo-600"></i>All Customers
                    </h2>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Phone</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Address</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Account Number</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                        <% if (customers != null && !customers.isEmpty()) { %>
                        <% for (CustomerDTO customer : customers) { %>
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= customer.getId() %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"><%= customer.getCustomerName() %></td>
                            <td class="px-6 py-4 text-sm text-gray-500"><%= customer.getCustomerEmail() %></td>
                            <td class="px-6 py-4 text-sm text-gray-500">
                                <%= customer.getCustomerPhone() != null ? customer.getCustomerPhone() : "No phone" %>
                            </td>
                            <td class="px-6 py-4 text-sm text-gray-500">
                                <%= customer.getCustomerAddress() != null ? customer.getCustomerAddress() : "No address" %>
                            </td>
                            <td class="px-6 py-4 text-sm text-gray-500">
                                <%= customer.getCustomerAccountNumber() != null ? customer.getCustomerAccountNumber() : "No account number" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                <button onclick="editCustomer(<%= customer.getId() %>)"
                                        class="text-indigo-600 hover:text-indigo-900 bg-indigo-50 hover:bg-indigo-100 px-3 py-1 rounded-md transition duration-200">
                                    <i class="fas fa-edit mr-1"></i>Edit
                                </button>
                                <button onclick="deleteCustomer(<%= customer.getId() %>, '<%= customer.getCustomerName() %>')"
                                        class="text-red-600 hover:text-red-900 bg-red-100 hover:bg-red-100 px-3 py-1 rounded-md transition duration-200">
                                    <i class="fas fa-trash mr-1"></i>Delete
                                </button>
                            </td>
                        </tr>
                        <% } %>
                        <% } else { %>
                        <tr>
                            <td colspan="7" class="px-6 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2 block"></i>
                                <%= request.getAttribute("customerDTOs") != null ? "No customers found. Add your first customer above." : "Loading customers..." %>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Edit Customer Modal -->
<div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-medium text-gray-900">
                    <i class="fas fa-edit mr-2 text-indigo-600"></i>Edit Customer
                </h3>
                <button onclick="closeEditModal()" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="editCustomerForm" action="${pageContext.request.contextPath}/customers/updateCustomer" method="post">
                <input type="hidden" id="editCustomerId" name="customerId">
                <div class="mb-4">
                    <label for="editCustomerName" class="block text-sm font-medium text-gray-700 mb-1">
                        Customer Name <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="editCustomerName" name="customerName" required
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Customer Name" />
                </div>
                <div class="mb-4">
                    <label for="editCustomerEmail" class="block text-sm font-medium text-gray-700 mb-1">
                        Email <span class="text-red-500">*</span>
                    </label>
                    <input type="email" id="editCustomerEmail" name="customerEmail" required
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Customer Email" />
                </div>
                <div class="mb-4">
                    <label for="editCustomerPhone" class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
                    <input type="text" id="editCustomerPhone" name="customerPhone"
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Customer Phone" />
                </div>
                <div class="mb-4">
                    <label for="editCustomerAddress" class="block text-sm font-medium text-gray-700 mb-1">Address</label>
                    <input type="text" id="editCustomerAddress" name="customerAddress"
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Customer Address" />
                </div>
                <div class="mb-4">
                    <label for="editCustomerAccountNumber" class="block text-sm font-medium text-gray-700 mb-1">Account Number</label>
                    <input type="text" id="editCustomerAccountNumber" name="customerAccountNumber"
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Customer Account Number" />
                </div>
                <div class="flex justify-end space-x-3">
                    <button type="button" onclick="closeEditModal()"
                            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                        Cancel
                    </button>
                    <button type="submit"
                            class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                        <i class="fas fa-save mr-1"></i>Update Customer
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
            <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 mb-4">
                <i class="fas fa-exclamation-triangle text-red-600 text-xl"></i>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">Delete Customer</h3>
            <p class="text-sm text-gray-500 mb-4">
                Are you sure you want to delete the customer "<span id="deleteCustomerName" class="font-medium"></span>"?
                This action cannot be undone.
            </p>
            <form id="deleteCustomerForm" action="${pageContext.request.contextPath}/customers/deleteCustomer" method="post">
                <input type="hidden" id="deleteCustomerId" name="customerId">
                <div class="flex justify-center space-x-3">
                    <button type="button" onclick="closeDeleteModal()"
                            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                        Cancel
                    </button>
                    <button type="submit"
                            class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500">
                        <i class="fas fa-trash mr-1"></i>Delete
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Auto-hide alerts after 5 seconds
    setTimeout(() => {
        const alerts = document.querySelectorAll('.bg-red-50, .bg-green-50');
        alerts.forEach(alert => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        });
    }, 5000);

    // Edit Customer Functions
    function editCustomer(customerId) {
        fetch('${pageContext.request.contextPath}/customers/getCustomer?id=' + customerId)
            .then(response => response.json())
            .then(data => {
                document.getElementById('editCustomerId').value = data.id;
                document.getElementById('editCustomerName').value = data.customerName;
                document.getElementById('editCustomerEmail').value = data.customerEmail;
                document.getElementById('editCustomerPhone').value = data.customerPhone || '';
                document.getElementById('editCustomerAddress').value = data.customerAddress || '';
                document.getElementById('editCustomerAccountNumber').value = data.customerAccountNumber || '';
                document.getElementById('editModal').classList.remove('hidden');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error loading customer data');
            });
    }

    function closeEditModal() {
        document.getElementById('editModal').classList.add('hidden');
        document.getElementById('editCustomerForm').reset();
    }

    // Delete Customer Functions
    function deleteCustomer(customerId, customerName) {
        document.getElementById('deleteCustomerId').value = customerId;
        document.getElementById('deleteCustomerName').textContent = customerName;
        document.getElementById('deleteModal').classList.remove('hidden');
    }

    function closeDeleteModal() {
        document.getElementById('deleteModal').classList.add('hidden');
    }

    // Close modals when clicking outside
    window.onclick = function(event) {
        const editModal = document.getElementById('editModal');
        const deleteModal = document.getElementById('deleteModal');

        if (event.target === editModal) {
            closeEditModal();
        }
        if (event.target === deleteModal) {
            closeDeleteModal();
        }
    }

    // Close modals with Escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            closeEditModal();
            closeDeleteModal();
        }
    });
</script>
</body>
</html>
