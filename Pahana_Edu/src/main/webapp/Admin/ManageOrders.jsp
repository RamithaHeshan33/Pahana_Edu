<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/31/2025
  Time: 11:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.pahana_edu.business.order.dto.OrderDTO" %>
<%@ page import="org.example.pahana_edu.business.order.dto.OrderItemDTO" %>
<%@ page import="org.example.pahana_edu.business.user.dto.UserResponseDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - Pahana Edu</title>
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
                        <h1 class="text-xl font-bold text-gray-900">Orders</h1>
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

            <!-- Orders Table -->
            <div class="bg-white rounded-xl shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-gray-200">
                    <h2 class="text-lg font-semibold text-gray-900">
                        <i class="fas fa-receipt mr-2 text-indigo-600"></i>All Orders
                    </h2>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order ID</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cash Received</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Change</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order Date</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                        <% if (orders != null && !orders.isEmpty()) { %>
                        <% for (OrderDTO order : orders) { %>
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#<%= order.getId() %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                <div>
                                    <div class="font-medium"><%= order.getCustomerName() != null ? order.getCustomerName() : "N/A" %></div>
                                    <div class="text-gray-500"><%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %></div>
                                </div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                <span class="font-medium text-green-600">Rs. <%= String.format("%.2f", order.getTotalAmount()) %></span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. <%= String.format("%.2f", order.getCashReceived()) %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. <%= String.format("%.2f", order.getChangeAmount()) %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                <%= order.getOrderDate() != null ? order.getOrderDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) : "N/A" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                    <%= order.getStatus() != null ? order.getStatus() : "completed" %>
                                </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                <button onclick="viewOrderDetails(<%= order.getId() %>)"
                                        class="text-indigo-600 hover:text-indigo-900 bg-indigo-50 hover:bg-indigo-100 px-3 py-1 rounded-md transition duration-200">
                                    <i class="fas fa-eye mr-1"></i>View Details
                                </button>
                            </td>
                        </tr>
                        <% } %>
                        <% } else { %>
                        <tr>
                            <td colspan="8" class="px-6 py-8 text-center text-gray-500">
                                <i class="fas fa-receipt text-4xl mb-2 block"></i>
                                No orders found.
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

<!-- Order Details Modal -->
<div id="orderDetailsModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-medium text-gray-900">
                    <i class="fas fa-receipt mr-2 text-indigo-600"></i>Order Details
                </h3>
                <button onclick="closeOrderDetailsModal()" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times"></i>
                </button>
            </div>

            <div id="orderDetailsContent">
                <!-- Order details will be populated here -->
            </div>
        </div>
    </div>
</div>

<script>
    function viewOrderDetails(orderId) {
        // Find the order in the orders list
        const orders = [
            <% if (orders != null && !orders.isEmpty()) { %>
            <% for (int i = 0; i < orders.size(); i++) { %>
            <% OrderDTO order = orders.get(i); %>
            {
                id: <%= order.getId() %>,
                customerName: "<%= order.getCustomerName() != null ? order.getCustomerName().replace("\"", "\\\"") : "N/A" %>",
                customerPhone: "<%= order.getCustomerPhone() != null ? order.getCustomerPhone() : "N/A" %>",
                totalAmount: <%= order.getTotalAmount() %>,
                cashReceived: <%= order.getCashReceived() %>,
                changeAmount: <%= order.getChangeAmount() %>,
                orderDate: "<%= order.getOrderDate() != null ? order.getOrderDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) : "N/A" %>",
                status: "<%= order.getStatus() != null ? order.getStatus() : "completed" %>",
                items: [
                    <% if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) { %>
                    <% for (int j = 0; j < order.getOrderItems().size(); j++) { %>
                    <% OrderItemDTO item = order.getOrderItems().get(j); %>
                    {
                        bookTitle: "<%= item.getBookTitle() != null ? item.getBookTitle().replace("\"", "\\\"") : "Unknown Book" %>",
                        quantity: <%= item.getQuantity() %>,
                        unitPrice: <%= item.getUnitPrice() %>,
                        totalPrice: <%= item.getTotalPrice() %>
                    }<%= j < order.getOrderItems().size() - 1 ? "," : "" %>
                    <% } %>
                    <% } %>
                ]
            }<%= i < orders.size() - 1 ? "," : "" %>
            <% } %>
            <% } %>
        ];

        const order = orders.find(o => o.id === orderId);
        if (!order) {
            alert('Order not found');
            return;
        }

        let itemsHtml = '';
        if (order.items && order.items.length > 0) {
            itemsHtml = '<div class="mt-4"><h4 class="font-medium text-gray-900 mb-2">Order Items:</h4><div class="bg-gray-50 rounded-lg p-4"><table class="min-w-full"><thead><tr class="text-left text-xs font-medium text-gray-500 uppercase tracking-wider"><th class="pb-2">Book</th><th class="pb-2">Quantity</th><th class="pb-2">Unit Price</th><th class="pb-2">Total</th></tr></thead><tbody>';

            order.items.forEach(item => {
                itemsHtml += '<tr class="border-t border-gray-200"><td class="py-2 text-sm text-gray-900">' + item.bookTitle + '</td><td class="py-2 text-sm text-gray-900">' + item.quantity + '</td><td class="py-2 text-sm text-gray-900">Rs. ' + item.unitPrice.toFixed(2) + '</td><td class="py-2 text-sm text-gray-900">Rs. ' + item.totalPrice.toFixed(2) + '</td></tr>';
            });

            itemsHtml += '</tbody></table></div></div>';
        } else {
            itemsHtml = '<div class="mt-4 text-gray-500">No items found for this order.</div>';
        }

        const detailsHtml =
            '<div class="grid grid-cols-1 md:grid-cols-2 gap-4">' +
            '<div>' +
            '<h4 class="font-medium text-gray-900 mb-2">Order Information</h4>' +
            '<div class="bg-gray-50 rounded-lg p-4 space-y-2">' +
            '<p><span class="font-medium">Order ID:</span> #' + order.id + '</p>' +
            '<p><span class="font-medium">Date:</span> ' + order.orderDate + '</p>' +
            '<p><span class="font-medium">Status:</span> <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">' + order.status + '</span></p>' +
            '</div>' +
            '</div>' +
            '<div>' +
            '<h4 class="font-medium text-gray-900 mb-2">Customer Information</h4>' +
            '<div class="bg-gray-50 rounded-lg p-4 space-y-2">' +
            '<p><span class="font-medium">Name:</span> ' + order.customerName + '</p>' +
            '<p><span class="font-medium">Phone:</span> ' + order.customerPhone + '</p>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="mt-4">' +
            '<h4 class="font-medium text-gray-900 mb-2">Payment Information</h4>' +
            '<div class="bg-gray-50 rounded-lg p-4 grid grid-cols-3 gap-4">' +
            '<div class="text-center">' +
            '<p class="text-sm text-gray-500">Total Amount</p>' +
            '<p class="text-lg font-bold text-green-600">Rs. ' + order.totalAmount.toFixed(2) + '</p>' +
            '</div>' +
            '<div class="text-center">' +
            '<p class="text-sm text-gray-500">Cash Received</p>' +
            '<p class="text-lg font-bold text-blue-600">Rs. ' + order.cashReceived.toFixed(2) + '</p>' +
            '</div>' +
            '<div class="text-center">' +
            '<p class="text-sm text-gray-500">Change Given</p>' +
            '<p class="text-lg font-bold text-purple-600">Rs. ' + order.changeAmount.toFixed(2) + '</p>' +
            '</div>' +
            '</div>' +
            '</div>' +
            itemsHtml;

        document.getElementById('orderDetailsContent').innerHTML = detailsHtml;
        document.getElementById('orderDetailsModal').classList.remove('hidden');
    }

    function closeOrderDetailsModal() {
        document.getElementById('orderDetailsModal').classList.add('hidden');
    }

    // Close modal when clicking outside
    window.onclick = function(event) {
        const modal = document.getElementById('orderDetailsModal');
        if (event.target === modal) {
            closeOrderDetailsModal();
        }
    }

    // Close modal with Escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            closeOrderDetailsModal();
        }
    });
</script>
</body>
</html>
