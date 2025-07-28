<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.example.pahana_edu.dto.UserResponseDTO" %>
<%
  UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/auth/login");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
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
            <h1 class="text-xl font-bold text-gray-900">Dashboard</h1>
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

      <!-- Quick Actions -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition duration-200">
          <div class="flex items-center">
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center mr-4">
              <i class="fas fa-book text-blue-600 text-xl"></i>
            </div>
            <div>
              <h4 class="font吴-semibold text-gray-900">Books</h4>
              <p class="text-sm text-gray-500">System book courses</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition duration-200">
          <div class="flex items-center">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mr-4">
              <i class="fas fa-list text-purple-600 text-xl"></i>
            </div>
            <div>
              <h4 class="font-semibold text-gray-900">Categories</h4>
              <p class="text-sm text-gray-500">View categories</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition duration-200">
          <div class="flex items-center">
            <div class="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center mr-4">
              <i class="fas fa-user text-orange-600 text-xl"></i>
            </div>
            <div>
              <h4 class="font-semibold text-gray-900">Customers</h4>
              <p class="text-sm text-gray-500">View customers</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition duration-200">
          <div class="flex items-center">
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mr-4">
              <i class="fas fa-chart-line text-green-600 text-xl"></i>
            </div>
            <div>
              <h4 class="font-semibold text-gray-900">Transactions</h4>
              <p class="text-sm text-gray-500">View transactions</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
