<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/17/2025
  Time: 2:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.example.pahana_edu.business.user.dto.UserResponseDTO" %>
<%@ page import="org.example.pahana_edu.business.category.dto.CategoryDTO" %>
<%@ page import="org.example.pahana_edu.business.book.dto.BookDTO" %>
<%@ page import="java.util.List" %>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    List<BookDTO> books = (List<BookDTO>) request.getAttribute("books");
    List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("categories");
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
                        <h1 class="text-xl font-bold text-gray-900">Books</h1>
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

            <!-- Add Book Form -->
            <div class="bg-white rounded-xl shadow-sm p-6 mb-8">
                <h3 class="text-xl font-semibold text-gray-900 mb-6">
                    <i class="fas fa-plus mr-2 text-indigo-600"></i>Add New Book
                </h3>

                <form id="bookForm" action="${pageContext.request.contextPath}/books/saveBook" method="post" enctype="multipart/form-data">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label for="title" class="block text-sm font-medium text-gray-700 mb-1">
                                Title <span class="text-red-500">*</span>
                            </label>
                            <input type="text" id="title" name="title" required
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter book title" />
                        </div>
                        <div>
                            <label for="author" class="block text-sm font-medium text-gray-700 mb-1">
                                Author
                            </label>
                            <input type="text" id="author" name="author"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter author name" />
                        </div>
                        <div>
                            <label for="publisher" class="block text-sm font-medium text-gray-700 mb-1">Publisher</label>
                            <input type="text" id="publisher" name="publisher"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter publisher name" />
                        </div>
                        <div>
                            <label for="isbn_number" class="block text-sm font-medium text-gray-700 mb-1">ISBN Number</label>
                            <input type="text" id="isbn_number" name="isbn_number"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter ISBN number" />
                        </div>
                        <div>
                            <label for="quantity" class="block text-sm font-medium text-gray-700 mb-1">Quantity <span class="text-red-500">*</span></label>
                            <input type="number" id="quantity" name="quantity" required min="0"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter quantity" />
                        </div>
                        <div>
                            <label for="price" class="block text-sm font-medium text-gray-700 mb-1">Price <span class="text-red-500">*</span></label>
                            <input type="number" id="price" name="price" required min="0" step="0.01"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                   placeholder="Enter price" />
                        </div>
                        <div>
                            <label for="category" class="block text-sm font-medium text-gray-700 mb-1">Category <span class="text-red-500">*</span></label>
                            <select id="category" name="category" required
                                    class="form-select block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                                <option value="">Select a category</option>
                                <% if (categories != null && !categories.isEmpty()) { %>
                                <% for (CategoryDTO category : categories) { %>
                                <option value="<%= category.getId() %>"><%= category.getCategoryName() %></option>
                                <% } %>
                                <% } %>
                            </select>
                        </div>
                        <div>
                            <label for="image" class="block text-sm font-medium text-gray-700 mb-1">Image</label>
                            <input type="file" id="image" name="image" accept="image/*"
                                   class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                        </div>
                        <div>
                            <label for="language" class="block text-sm font-medium text-gray-700 mb-1">Language</label>
                            <select id="language" name="language"
                                    class="form-select block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                                <option value="" disabled selected>Select a language</option>
                                <option value="english">English</option>
                                <option value="sinhala">Sinhala</option>
                                <option value="tamil">Tamil</option>
                            </select>
                        </div>
                        <div class="md:col-span-2">
                            <label for="description" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                            <textarea id="description" name="description" rows="4"
                                      class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                                      placeholder="Enter book description"></textarea>
                        </div>
                    </div>
                    <div class="mt-6 flex justify-end space-x-3">
                        <button type="button" onclick="document.getElementById('bookForm').reset()"
                                class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                            Reset
                        </button>
                        <button type="submit"
                                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                            <i class="fas fa-plus mr-1"></i>
                            Add Book
                        </button>
                    </div>
                </form>
            </div>

            <!-- Books Display -->
            <div class="bg-white rounded-xl shadow-sm p-6">
                <div class="flex items-center justify-between mb-6">
                    <h3 class="text-xl font-semibold text-gray-900">
                        <i class="fas fa-books mr-2 text-indigo-600"></i>All Books
                    </h3>
                    <span class="text-sm text-gray-500">
                        <%= books != null ? books.size() : 0 %> books found
                    </span>
                </div>

                <% if (books != null && !books.isEmpty()) { %>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    <% for (BookDTO book : books) { %>
                    <div class="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition duration-200">
                        <div class="p-4">
                            <div class="mb-3">
                                <% if (book.getImage() != null && !book.getImage().isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/<%= book.getImage() %>" alt="<%= book.getTitle() %>" class="w-full h-40 object-cover rounded-md mb-3">
                                <% } else { %>
                                <img src="${pageContext.request.contextPath}/images/default-book.jpg" alt="No image" class="w-full h-40 object-cover rounded-md mb-3">
                                <% } %>
                            </div>
                            <div class="flex items-start justify-between mb-3">
                                <div class="flex-1">
                                    <h4 class="font-semibold text-gray-900 text-sm mb-1 line-clamp-2">
                                        <%= book.getTitle() %>
                                    </h4>
                                    <p class="text-xs text-gray-600 mb-1">
                                        <i class="fas fa-user mr-1"></i><%= book.getAuthor() %>
                                    </p>
                                    <p class="text-xs text-gray-500">
                                        <i class="fas fa-tag mr-1"></i><%= book.getCategoryName() != null ? book.getCategoryName() : "No Category" %>
                                    </p>
                                </div>
                                <div class="ml-2">
                                    <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                        Rs. <%= String.format("%.2f", book.getPrice()) %>
                                    </span>
                                </div>
                            </div>
                            <div class="flex items-center justify-between text-xs text-gray-500 mb-3">
                                <span><i class="fas fa-boxes mr-1"></i>Qty: <%= book.getQuantity() %></span>
                                <span><i class="fas fa-language mr-1"></i><%= book.getLanguage() %></span>
                            </div>
                            <div class="text-xs text-gray-600 mb-3">
                                <p class="truncate">ISBN: <%= book.getIsbn() %></p>
                                <% if (book.getPublisher() != null && !book.getPublisher().isEmpty()) { %>
                                <p class="truncate">Publisher: <%= book.getPublisher() %></p>
                                <% } %>
                            </div>
                            <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                            <p class="text-xs text-gray-600 mb-3 line-clamp-2">
                                <%= book.getDescription() %>
                            </p>
                            <% } %>
                            <div class="flex space-x-2">
                                <button onclick="editBook(<%= book.getId() %>)"
                                        class="flex-1 text-xs bg-indigo-50 text-indigo-600 hover:bg-indigo-100 px-3 py-2 rounded-md transition duration-200">
                                    <i class="fas fa-edit mr-1"></i>Edit
                                </button>
                                <button onclick="deleteBook(<%= book.getId() %>, '<%= book.getTitle().replace("'", "\\'") %>')"
                                        class="flex-1 text-xs bg-red-100 text-red-600 hover:bg-red-100 px-3 py-2 rounded-md transition duration-200">
                                    <i class="fas fa-trash mr-1"></i>Delete
                                </button>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
                <% } else { %>
                <div class="text-center py-12">
                    <i class="fas fa-book text-6xl text-gray-300 mb-4"></i>
                    <h3 class="text-lg font-medium text-gray-900 mb-2">No books found</h3>
                    <p class="text-gray-500">Add your first book using the form above.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<!-- Edit Book Modal -->
<div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
    <div class="relative top-10 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
                <h3 class="text-lg font-medium text-gray-900">
                    <i class="fas fa-edit mr-2 text-indigo-600"></i>Edit Book
                </h3>
                <button onclick="closeEditModal()" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="editBookForm" action="${pageContext.request.contextPath}/books/updateBook" method="post" enctype="multipart/form-data">
                <input type="hidden" id="editBookId" name="bookId">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label for="editTitle" class="block text-sm font-medium text-gray-700 mb-1">
                            Title <span class="text-red-500">*</span>
                        </label>
                        <input type="text" id="editTitle" name="title" required
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editAuthor" class="block text-sm font-medium text-gray-700 mb-1">
                            Author
                        </label>
                        <input type="text" id="editAuthor" name="author"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editPublisher" class="block text-sm font-medium text-gray-700 mb-1">Publisher</label>
                        <input type="text" id="editPublisher" name="publisher"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editIsbn" class="block text-sm font-medium text-gray-700 mb-1">ISBN Number</label>
                        <input type="text" id="editIsbn" name="isbn_number"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editQuantity" class="block text-sm font-medium text-gray-700 mb-1">Quantity <span class="text-red-500">*</span></label>
                        <input type="number" id="editQuantity" name="quantity" required min="0"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editPrice" class="block text-sm font-medium text-gray-700 mb-1">Price <span class="text-red-500">*</span></label>
                        <input type="number" id="editPrice" name="price" required min="0" step="0.01"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editCategory" class="block text-sm font-medium text-gray-700 mb-1">Category <span class="text-red-500">*</span></label>
                        <select id="editCategory" name="category" required
                                class="form-select block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                            <option value="" disabled>Select a category</option>
                            <% if (categories != null && !categories.isEmpty()) { %>
                            <% for (CategoryDTO category : categories) { %>
                            <option value="<%= category.getId() %>"><%= category.getCategoryName() %></option>
                            <% } %>
                            <% } %>
                        </select>
                    </div>
                    <div>
                        <label for="editImage" class="block text-sm font-medium text-gray-700 mb-1">Image</label>
                        <input type="file" id="editImage" name="image" accept="image/*"
                               class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" />
                    </div>
                    <div>
                        <label for="editLanguage" class="block text-sm font-medium text-gray-700 mb-1">Language</label>
                        <select id="editLanguage" name="language"
                                class="form-select block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                            <option value="">Select a language</option>
                            <option value="english">English</option>
                            <option value="sinhala">Sinhala</option>
                            <option value="tamil">Tamil</option>
                        </select>
                    </div>
                    <div class="md:col-span-2">
                        <label for="editDescription" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                        <textarea id="editDescription" name="description" rows="3"
                                  class="form-input block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"></textarea>
                    </div>
                </div>
                <div class="flex justify-end space-x-3 mt-6">
                    <button type="button" onclick="closeEditModal()"
                            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
                        Cancel
                    </button>
                    <button type="submit"
                            class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                        <i class="fas fa-save mr-1"></i>Update Book
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
            <h3 class="text-lg font-medium text-gray-900 mb-2">Delete Book</h3>
            <p class="text-sm text-gray-500 mb-4">
                Are you sure you want to delete "<span id="deleteBookTitle" class="font-medium"></span>"?
                This action cannot be undone.
            </p>
            <form id="deleteBookForm" action="${pageContext.request.contextPath}/books/deleteBook" method="post">
                <input type="hidden" id="deleteBookId" name="bookId">
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

<style>
    .line-clamp-2 {
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
</style>

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

    // Edit Book Functions
    function editBook(bookId) {
        fetch('${pageContext.request.contextPath}/books/getBook?id=' + bookId)
            .then(response => response.json())
            .then(data => {
                document.getElementById('editBookId').value = data.id;
                document.getElementById('editTitle').value = data.title;
                document.getElementById('editAuthor').value = data.author;
                document.getElementById('editPublisher').value = data.publisher || '';
                document.getElementById('editIsbn').value = data.isbn;
                document.getElementById('editQuantity').value = data.quantity;
                document.getElementById('editPrice').value = data.price;
                document.getElementById('editLanguage').value = data.language;
                document.getElementById('editDescription').value = data.description || '';
                const categorySelect = document.getElementById('editCategory');
                for (let option of categorySelect.options) {
                    if (option.text === data.categoryName) {
                        option.selected = true;
                        break;
                    }
                }
                document.getElementById('editModal').classList.remove('hidden');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error loading book data');
            });
    }

    function closeEditModal() {
        document.getElementById('editModal').classList.add('hidden');
        document.getElementById('editBookForm').reset();
    }

    // Delete Book Functions
    function deleteBook(bookId, bookTitle) {
        document.getElementById('deleteBookId').value = bookId;
        document.getElementById('deleteBookTitle').textContent = bookTitle;
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
