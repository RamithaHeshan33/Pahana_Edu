<%-- Remove the page directive --%>
<div class="fixed inset-y-0 left-0 w-64 bg-white shadow-sm border-r z-40">
  <div class="flex flex-col h-full">
    <!-- Sidebar Header -->
    <div class="flex items-center px-4 py-4 border-b">
      <div class="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center mr-3">
        <i class="fas fa-graduation-cap text-white"></i>
      </div>
      <h1 class="text-xl font-bold text-gray-900">Pahana Edu</h1>
    </div>

    <!-- Sidebar Navigation -->
    <nav class="flex-1 px-4 py-6 space-y-2">
      <a href="${pageContext.request.contextPath}/auth/dashboard"
         class="flex items-center px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-600 rounded-lg transition duration-200">
        <i class="fas fa-home mr-3"></i>
        Dashboard
      </a>
      <a href="${pageContext.request.contextPath}/books/ManageBooks"
         class="flex items-center px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-600 rounded-lg transition duration-200">
        <i class="fas fa-book mr-3"></i>
        Books
      </a>
      <a href="${pageContext.request.contextPath}/categories/ManageCategories"
         class="flex items-center px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-600 rounded-lg transition duration-200">
        <i class="fas fa-list mr-3"></i>
        Categories
      </a>
      <a href="${pageContext.request.contextPath}/customers"
         class="flex items-center px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-600 rounded-lg transition duration-200">
        <i class="fas fa-circle-user mr-3"></i>
        Customers
      </a>
      <a href="${pageContext.request.contextPath}/transactions"
         class="flex items-center px-4 py-2 text-gray-700 hover:bg-indigo-50 hover:text-indigo-600 rounded-lg transition duration-200">
        <i class="fas fa-dollar-sign mr-3"></i>
        Transactions
      </a>
    </nav>

    <!-- Sidebar Footer -->
    <div class="px-4 py-4 border-t">
      <a href="${pageContext.request.contextPath}/auth/logout"
         class="flex items-center px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition duration-200">
        <i class="fas fa-sign-out-alt mr-2"></i>
        Logout
      </a>
    </div>
  </div>
</div>