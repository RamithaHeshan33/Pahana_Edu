<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2025
  Time: 7:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.pahana_edu.business.user.dto.UserResponseDTO" %>
<%@ page import="org.example.pahana_edu.business.category.dto.CategoryDTO" %>
<%@ page import="java.util.List" %>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("categories");
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - Pahana Edu</title>
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
                        <h1 class="text-xl font-bold text-gray-900">Categories</h1>
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

            <!-- Add Category Form -->
            <div class="bg-white rounded-xl shadow-sm p-6 mb-8">
                <h2 class="text-lg font-semibold text-gray-900 mb-4">
                    <i class="fas fa-plus mr-2 text-indigo-600"></i>Add New Category
                </h2>
                <form id="categoryForm" action="${pageContext.request.contextPath}/categories/saveCategory" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label for="categoryName" class="block text-sm font-medium text-gray-700 mb-1">
                                Category Name <span class="text-red-500">*</span>
                            </label>
                            <input type="text" id="categoryName" name="categoryName" required
                                   value="<%= request.getAttribute("categoryName") != null ? request.getAttribute("categoryName") : "" %>"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter Category Name" />
                        </div>
                        <div>
                            <label for="categoryDescription" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                            <textarea id="categoryDescription" name="categoryDescription" rows="3"
                                      class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                      placeholder="Enter Category Description"><%= request.getAttribute("categoryDescription") != null ? request.getAttribute("categoryDescription") : "" %></textarea>
                        </div>
                    </div>
                    <div class="mt-4 flex justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('categoryForm').reset()"
                                class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                            Reset
                        </button>
                        <button type="submit"
                                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                            <i class="fas fa-plus mr-1"></i>Add Category
                        </button>
                    </div>
                </form>
            </div>

            <!-- Categories Table -->
            <div class="bg-white rounded-xl shadow-sm overflow-hidden">
                <div class="px-6 py-4 border-b border-gray-200">
                    <h2 class="text-lg font-semibold text-gray-900">
                        <i class="fas fa-list mr-2 text-indigo-600"></i>All Categories
                    </h2>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                        <% if (categories != null && !categories.isEmpty()) { %>
                        <% for (CategoryDTO category : categories) { %>
                        <tr class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= category.getId() %></td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"><%= category.getCategoryName() %></td>
                            <td class="px-6 py-4 text-sm text-gray-500">
                                <%= category.getCategoryDescription() != null ? category.getCategoryDescription() : "No description" %>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                <button onclick="editCategory(<%= category.getId() %>)"
                                        class="text-indigo-600 hover:text-indigo-900 bg-indigo-50 hover:bg-indigo-100 px-3 py-1 rounded-md transition duration-200">
                                    <i class="fas fa-edit mr-1"></i>Edit
                                </button>
                                <button onclick="deleteCategory(<%= category.getId() %>, '<%= category.getCategoryName() %>')"
                                        class="text-red-600 hover:text-red-900 bg-red-100 hover:bg-red-100 px-3 py-1 rounded-md transition duration-200">
                                    <i class="fas fa-trash mr-1"></i>Delete
                                </button>
                            </td>
                        </tr>
                        <% } %>
                        <% } else { %>
                        <tr>
                            <td colspan="4" class="px-6 py-8 text-center text-gray-500">
                                <i class="fas fa-inbox text-4xl mb-2 block"></i>
                                No categories found. Add your first category above.
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

<!-- Edit Category Modal -->
<div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-medium text-gray-900">
                    <i class="fas fa-edit mr-2 text-indigo-600"></i>Edit Category
                </h3>
                <button onclick="closeEditModal()" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="editCategoryForm" action="${pageContext.request.contextPath}/categories/updateCategory" method="post">
                <input type="hidden" id="editCategoryId" name="categoryId">
                <div class="mb-4">
                    <label for="editCategoryName" class="block text-sm font-medium text-gray-700 mb-1">
                        Category Name <span class="text-red-500">*</span>
                    </label>
                    <input type="text" id="editCategoryName" name="categoryName" required
                           class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                           placeholder="Enter Category Name" />
                </div>
                <div class="mb-4">
                    <label for="editCategoryDescription" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                    <textarea id="editCategoryDescription" name="categoryDescription" rows="3"
                              class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                              placeholder="Enter Category Description"></textarea>
                </div>
                <div class="flex justify-end space-x-3">
                    <button type="button" onclick="closeEditModal()"
                            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                        Cancel
                    </button>
                    <button type="submit"
                            class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                        <i class="fas fa-save mr-1"></i>Update Category
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
            <h3 class="text-lg font-medium text-gray-900 mb-2">Delete Category</h3>
            <p class="text-sm text-gray-500 mb-4">
                Are you sure you want to delete the category "<span id="deleteCategoryName" class="font-medium"></span>"?
                This action cannot be undone.
            </p>
            <form id="deleteCategoryForm" action="${pageContext.request.contextPath}/categories/deleteCategory" method="post">
                <input type="hidden" id="deleteCategoryId" name="categoryId">
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

    // Edit Category Functions
    function editCategory(categoryId) {
        fetch('${pageContext.request.contextPath}/categories/getCategory?id=' + categoryId)
            .then(response => response.json())
            .then(data => {
                document.getElementById('editCategoryId').value = data.id;
                document.getElementById('editCategoryName').value = data.name;
                document.getElementById('editCategoryDescription').value = data.description || '';
                document.getElementById('editModal').classList.remove('hidden');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error loading category data');
            });
    }

    function closeEditModal() {
        document.getElementById('editModal').classList.add('hidden');
        document.getElementById('editCategoryForm').reset();
    }

    // Delete Category Functions
    function deleteCategory(categoryId, categoryName) {
        document.getElementById('deleteCategoryId').value = categoryId;
        document.getElementById('deleteCategoryName').textContent = categoryName;
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
