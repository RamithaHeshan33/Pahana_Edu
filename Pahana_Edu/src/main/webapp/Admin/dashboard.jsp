<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.example.pahana_edu.business.user.dto.UserResponseDTO" %>
<%@ page import="org.example.pahana_edu.business.book.dto.BookDTO" %>
<%@ page import="org.example.pahana_edu.business.category.dto.CategoryDTO" %>
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
            <!-- Cart Icon -->
            <div class="relative">
              <button onclick="toggleCart()" class="text-gray-700 hover:text-indigo-600 transition duration-200">
                <i class="fas fa-shopping-cart text-xl"></i>
                <span id="cartCount" class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center hidden">0</span>
              </button>
            </div>
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
      <!-- Search Section -->
      <div class="bg-white rounded-xl shadow-sm p-6 mb-8">
        <h2 class="text-lg font-semibold text-gray-900 mb-4">
          <i class="fas fa-search mr-2 text-indigo-600"></i>Search Books
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label for="searchName" class="block text-sm font-medium text-gray-700 mb-1">Book Name</label>
            <input type="text" id="searchName" placeholder="Search by book name..."
                   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
          </div>
          <div>
            <label for="searchCategory" class="block text-sm font-medium text-gray-700 mb-1">Category</label>
            <select id="searchCategory" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
              <option value="">All Categories</option>
              <% if (categories != null && !categories.isEmpty()) { %>
              <% for (CategoryDTO category : categories) { %>
              <option value="<%= category.getCategoryName() %>"><%= category.getCategoryName() %></option>
              <% } %>
              <% } %>
            </select>
          </div>
          <div>
            <label for="searchISBN" class="block text-sm font-medium text-gray-700 mb-1">ISBN</label>
            <input type="text" id="searchISBN" placeholder="Search by ISBN..."
                   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
          </div>
          <div>
            <label for="searchAuthor" class="block text-sm font-medium text-gray-700 mb-1">Author</label>
            <input type="text" id="searchAuthor" placeholder="Search by Author..."
                   class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
          </div>
        </div>
        <div class="mt-4 flex justify-end space-x-3">
          <button onclick="clearSearch()" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
            Clear
          </button>
          <button onclick="searchBooks()" class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
            <i class="fas fa-search mr-1"></i>Search
          </button>
        </div>
      </div>

      <!-- Books Display -->
      <div class="bg-white rounded-xl shadow-sm p-6">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-lg font-semibold text-gray-900">
            <i class="fas fa-book mr-2 text-indigo-600"></i>Available Books
          </h2>
          <span class="text-sm text-gray-500">
            <span id="bookCount"><%= books != null ? books.size() : 0 %></span> books found
          </span>
        </div>

        <div id="booksContainer">
          <% if (books != null && !books.isEmpty()) { %>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6" id="booksGrid">
            <% for (BookDTO book : books) { %>
            <div class="book-card bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition duration-200"
                 data-name="<%= book.getTitle() != null ? book.getTitle().toLowerCase() : "" %>"
                 data-category="<%= book.getCategoryName() != null ? book.getCategoryName().toLowerCase() : "" %>"
                 data-isbn="<%= book.getIsbn() != null ? book.getIsbn().toLowerCase() : "" %>"
                 data-author="<%= book.getAuthor() != null ? book.getAuthor().toLowerCase() : "" %>">
              <div class="p-4">
                <div class="mb-3">
                  <% if (book.getImage() != null && !book.getImage().isEmpty()) { %>
                  <img src="${pageContext.request.contextPath}/<%= book.getImage() %>" alt="<%= book.getTitle() != null ? book.getTitle() : "Book Image" %>" class="w-full h-40 object-cover rounded-md mb-3">
                  <% } else { %>
                  <div class="w-full h-40 bg-gray-200 rounded-md mb-3 flex items-center justify-center">
                    <i class="fas fa-book text-gray-400 text-3xl"></i>
                  </div>
                  <% } %>
                </div>
                <div class="mb-3">
                  <h4 class="font-semibold text-gray-900 text-sm mb-1 line-clamp-2">
                    <%= book.getTitle() != null ? book.getTitle() : "Untitled" %>
                  </h4>
                  <p class="text-xs text-gray-600 mb-1">
                    <i class="fas fa-user mr-1"></i><%= book.getAuthor() != null ? book.getAuthor() : "Unknown Author" %>
                  </p>
                  <p class="text-xs text-gray-500 mb-1">
                    <i class="fas fa-tag mr-1"></i><%= book.getCategoryName() != null ? book.getCategoryName() : "No Category" %>
                  </p>
                  <p class="text-xs text-gray-500">
                    <i class="fas fa-barcode mr-1"></i>ISBN: <%= book.getIsbn() != null ? book.getIsbn() : "N/A" %>
                  </p>
                </div>
                <div class="flex items-center justify-between mb-3">
                  <span class="text-lg font-bold text-green-600">
                    Rs. <%= book.getPrice() != null ? String.format("%.2f", book.getPrice()) : "0.00" %>
                  </span>
                  <span class="text-xs text-gray-500">
                    <i class="fas fa-boxes mr-1"></i>Stock: <%= book.getQuantity() != null ? book.getQuantity() : 0 %>
                  </span>
                </div>
                <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                <p class="text-xs text-gray-600 mb-3 line-clamp-2">
                  <%= book.getDescription() %>
                </p>
                <% } %>
                <div class="flex space-x-2">
                  <div class="flex items-center space-x-1 flex-1">
                    <button onclick="decreaseQuantity(<%= book.getId() %>)" class="w-8 h-8 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 flex items-center justify-center">
                      <i class="fas fa-minus text-xs"></i>
                    </button>
                    <input type="number" id="qty-<%= book.getId() %>" value="1" min="1" max="<%= book.getQuantity() != null ? book.getQuantity() : 1 %>"
                           class="w-12 text-center text-xs border border-gray-300 rounded-md">
                    <button onclick="increaseQuantity(<%= book.getId() %>)" class="w-8 h-8 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 flex items-center justify-center">
                      <i class="fas fa-plus text-xs"></i>
                    </button>
                  </div>
                  <button onclick="addToCart(<%= book.getId() %>, '<%= book.getTitle() != null ? book.getTitle().replace("'", "\\'") : "Untitled" %>', <%= book.getPrice() != null ? book.getPrice() : 0 %>, '<%= book.getImage() != null ? book.getImage() : "" %>', <%= book.getQuantity() != null ? book.getQuantity() : 0 %>)"
                          class="flex-1 text-xs bg-indigo-600 text-white hover:bg-indigo-700 px-3 py-2 rounded-md transition duration-200">
                    <i class="fas fa-cart-plus mr-1"></i>Add to Cart
                  </button>
                </div>
              </div>
            </div>
            <% } %>
          </div>
          <% } else { %>
          <div class="text-center py-12">
            <i class="fas fa-book text-6xl text-gray-300 mb-4"></i>
            <h3 class="text-lg font-medium text-gray-900 mb-2">No books available</h3>
            <p class="text-gray-500">Check back later for new arrivals.</p>
          </div>
          <% } %>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Shopping Cart Sidebar -->
<div id="cartSidebar" class="fixed inset-y-0 right-0 w-96 bg-white shadow-lg transform translate-x-full transition-transform duration-300 ease-in-out z-50">
  <div class="flex flex-col h-full">
    <!-- Cart Header -->
    <div class="flex items-center justify-between p-4 border-b">
      <h3 class="text-lg font-semibold text-gray-900">
        <i class="fas fa-shopping-cart mr-2"></i>Shopping Cart
      </h3>
      <button onclick="toggleCart()" class="text-gray-400 hover:text-gray-600">
        <i class="fas fa-times"></i>
      </button>
    </div>

    <!-- Cart Items -->
    <div class="flex-1 overflow-y-auto p-4">
      <div id="cartItems">
        <div class="text-center py-8 text-gray-500">
          <i class="fas fa-shopping-cart text-4xl mb-2"></i>
          <p>Your cart is empty</p>
        </div>
      </div>
    </div>

    <!-- Cart Footer -->
    <div class="border-t p-4">
      <div class="flex items-center justify-between mb-4">
        <span class="text-lg font-semibold text-gray-900">Total:</span>
        <span id="cartTotal" class="text-lg font-bold text-green-600">Rs. 0.00</span>
      </div>
      <button id="checkoutBtn" onclick="checkout()" disabled
              class="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-300 disabled:cursor-not-allowed">
        <i class="fas fa-credit-card mr-2"></i>Proceed to Checkout
      </button>
    </div>
  </div>
</div>

<!-- Cart Overlay -->
<div id="cartOverlay" class="fixed inset-0 bg-black bg-opacity-50 z-40 hidden" onclick="toggleCart()"></div>

<!-- Checkout Modal -->
<div id="checkoutModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
  <div class="relative top-10 mx-auto p-5 border w-full max-w-2xl shadow-lg rounded-md bg-white">
    <div class="mt-3">
      <div class="flex items-center justify-between mb-4">
        <h3 class="text-lg font-medium text-gray-900">
          <i class="fas fa-credit-card mr-2 text-indigo-600"></i>Checkout
        </h3>
        <button onclick="closeCheckoutModal()" class="text-gray-400 hover:text-gray-600">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <form id="checkoutForm" onsubmit="event.preventDefault();">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Customer Verification Section -->
          <div>
            <h4 class="text-md font-semibold text-gray-900 mb-3">Customer Information</h4>
            <div class="space-y-4">
              <div>
                <label for="customerPhone" class="block text-sm font-medium text-gray-700 mb-1">
                  Customer Phone Number <span class="text-red-500">*</span>
                </label>
                <div class="flex space-x-2">
                  <input type="text" id="customerPhone" required
                         class="flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                         placeholder="Enter customer phone number">
                  <button type="button" id="verifyBtn" onclick="verifyCustomer()"
                          class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
                    <i class="fas fa-search mr-1"></i>Verify
                  </button>
                </div>
              </div>

              <!-- Customer Info Display (Hidden by default) -->
              <div id="customerInfo" class="hidden bg-green-50 border border-green-200 rounded-lg p-3">
                <h5 class="text-sm font-medium text-green-800 mb-2">Customer Verified</h5>
                <p class="text-sm text-green-700">
                  <strong>Name:</strong> <span id="customerName"></span><br>
                  <strong>Phone:</strong> <span id="customerPhoneDisplay"></span>
                </p>
              </div>
            </div>
          </div>

          <!-- Order Summary Section -->
          <div>
            <h4 class="text-md font-semibold text-gray-900 mb-3">Order Summary</h4>
            <div class="bg-gray-50 rounded-lg p-4 max-h-64 overflow-y-auto">
              <div id="checkoutSummary">
                <!-- Order summary will be populated here -->
              </div>
            </div>
          </div>
        </div>

        <!-- Payment Section (Hidden by default) -->
        <div id="paymentSection" class="hidden mt-6">
          <h4 class="text-md font-semibold text-gray-900 mb-3">Payment Information</h4>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label for="totalAmountDisplay" class="block text-sm font-medium text-gray-700 mb-1">Total Amount</label>
              <div class="px-3 py-2 bg-gray-100 border border-gray-300 rounded-md text-lg font-bold text-green-600">
                Rs. <span id="totalAmountDisplay"></span>
              </div>
            </div>
            <div>
              <label for="cashReceived" class="block text-sm font-medium text-gray-700 mb-1">
                Cash Received <span class="text-red-500">*</span>
              </label>
              <input type="number" id="cashReceived" step="0.01" min="0" required
                     onchange="calculateChange()" oninput="calculateChange()"
                     class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                     placeholder="Enter cash received">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Change Amount</label>
              <div class="px-3 py-2 bg-gray-100 border border-gray-300 rounded-md">
                <span id="changeAmount" class="text-lg font-bold">Rs. 0.00</span>
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-end space-x-3 mt-6">
          <button type="button" onclick="closeCheckoutModal()"
                  class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500">
            Cancel
          </button>
          <button type="button" id="processBtn" onclick="processCheckout()" disabled
                  class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-300 disabled:cursor-not-allowed">
            <i class="fas fa-check mr-1"></i>Complete Order
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Order Success Modal -->
<div id="orderSuccessModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full hidden z-50">
  <div class="relative top-20 mx-auto p-5 border w-full max-w-md shadow-lg rounded-md bg-white">
    <div class="mt-3 text-center">
      <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-green-100 mb-4">
        <i class="fas fa-check text-green-600 text-xl"></i>
      </div>
      <h3 class="text-lg font-medium text-gray-900 mb-4">Order Completed Successfully!</h3>

      <div class="bg-gray-50 rounded-lg p-4 mb-4 text-left">
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span class="font-medium">Order ID:</span>
            <span id="successOrderId"></span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Customer:</span>
            <span id="successCustomerName"></span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Total Amount:</span>
            <span id="successTotalAmount" class="text-green-600 font-bold"></span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Cash Received:</span>
            <span id="successCashReceived"></span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Change:</span>
            <span id="successChangeAmount" class="text-blue-600 font-bold"></span>
          </div>
        </div>
      </div>

      <button onclick="closeOrderSuccessModal()"
              class="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500">
        <i class="fas fa-check mr-2"></i>OK
      </button>
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
  let cart = [];
  let cartTotal = 0;

  // Search functionality
  function searchBooks() {
    const nameSearch = document.getElementById('searchName').value.toLowerCase();
    const categorySearch = document.getElementById('searchCategory').value.toLowerCase();
    const isbnSearch = document.getElementById('searchISBN').value.toLowerCase();
    const authorSearch = document.getElementById('searchAuthor').value.toLowerCase();

    const bookCards = document.querySelectorAll('.book-card');
    let visibleCount = 0;

    bookCards.forEach(card => {
      const name = card.dataset.name;
      const category = card.dataset.category;
      const isbn = card.dataset.isbn;
      const author = card.dataset.author;

      const nameMatch = !nameSearch || name.includes(nameSearch);
      const categoryMatch = !categorySearch || category.includes(categorySearch);
      const isbnMatch = !isbnSearch || isbn.includes(isbnSearch);
      const authorMatch = !authorSearch || author.includes(authorSearch);

      if (nameMatch && categoryMatch && isbnMatch && authorMatch) {
        card.style.display = 'block';
        visibleCount++;
      } else {
        card.style.display = 'none';
      }
    });

    document.getElementById('bookCount').textContent = visibleCount;
  }

  function clearSearch() {
    document.getElementById('searchName').value = '';
    document.getElementById('searchCategory').value = '';
    document.getElementById('searchISBN').value = '';
    document.getElementById('searchAuthor').value = '';

    const bookCards = document.querySelectorAll('.book-card');
    bookCards.forEach(card => {
      card.style.display = 'block';
    });

    document.getElementById('bookCount').textContent = bookCards.length;
  }

  // Quantity controls
  function increaseQuantity(bookId) {
    const qtyInput = document.getElementById('qty-' + bookId);
    const currentQty = parseInt(qtyInput.value);
    const maxQty = parseInt(qtyInput.max);

    if (currentQty < maxQty) {
      qtyInput.value = currentQty + 1;
    }
  }

  function decreaseQuantity(bookId) {
    const qtyInput = document.getElementById('qty-' + bookId);
    const currentQty = parseInt(qtyInput.value);

    if (currentQty > 1) {
      qtyInput.value = currentQty - 1;
    }
  }

  // Cart functionality
  function addToCart(bookId, title, price, image, stock) {
    const qtyInput = document.getElementById('qty-' + bookId);
    const quantity = parseInt(qtyInput.value);

    // Check if item already exists in cart
    const existingItemIndex = cart.findIndex(item => item.id === bookId);

    if (existingItemIndex > -1) {
      // Update existing item
      const newQuantity = cart[existingItemIndex].quantity + quantity;
      if (newQuantity <= stock) {
        cart[existingItemIndex].quantity = newQuantity;
      } else {
        alert('Cannot add more items. Stock limit reached.');
        return;
      }
    } else {
      // Add new item
      cart.push({
        id: bookId,
        title: title,
        price: price,
        image: image,
        quantity: quantity,
        stock: stock
      });
    }

    // Sync cart with server
    syncCartWithServer();

    updateCartDisplay();
    qtyInput.value = 1;

    // Show success message
    showNotification('Item added to cart successfully!', 'success');
  }

  function removeFromCart(bookId) {
    cart = cart.filter(item => item.id !== bookId);
    syncCartWithServer();
    updateCartDisplay();
    showNotification('Item removed from cart', 'info');
  }

  function updateCartItemQuantity(bookId, newQuantity) {
    const item = cart.find(item => item.id === bookId);
    if (item) {
      if (newQuantity <= 0) {
        removeFromCart(bookId);
      } else if (newQuantity <= item.stock) {
        item.quantity = newQuantity;
        syncCartWithServer();
        updateCartDisplay();
      } else {
        alert('Cannot exceed stock limit');
        document.getElementById('cart-qty-' + bookId).value = item.quantity;
      }
    }
  }

  // Sync cart with server session
  function syncCartWithServer() {
    // Clear existing cart on server
    fetch('${pageContext.request.contextPath}/cart/clear', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      }
    }).then(() => {
      // Add each item to server cart
      cart.forEach(item => {
        const formData = new URLSearchParams();
        formData.append('bookId', item.id);
        formData.append('title', item.title);
        formData.append('price', item.price);
        formData.append('quantity', item.quantity);
        formData.append('image', item.image || '');

        fetch('${pageContext.request.contextPath}/cart/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: formData.toString()
        }).catch(error => {
          console.error('Error syncing cart item:', error);
        });
      });
    }).catch(error => {
      console.error('Error clearing server cart:', error);
    });
  }

  // Load cart from server on page load
  function loadCartFromServer() {
    fetch('${pageContext.request.contextPath}/cart/get')
            .then(response => response.json())
            .then(data => {
              if (data.items && data.items.length > 0) {
                cart = data.items.map(item => ({
                  id: item.bookId,
                  title: item.title,
                  price: item.price,
                  image: item.image,
                  quantity: item.quantity,
                  stock: 999
                }));
                updateCartDisplay();
              }
            })
            .catch(error => {
              console.error('Error loading cart from server:', error);
            });
  }

  function updateCartDisplay() {
    const cartItemsContainer = document.getElementById('cartItems');
    const cartCount = document.getElementById('cartCount');
    const cartTotalElement = document.getElementById('cartTotal');
    const checkoutBtn = document.getElementById('checkoutBtn');

    if (cart.length === 0) {
      cartItemsContainer.innerHTML = '<div class="text-center py-8 text-gray-500"><i class="fas fa-shopping-cart text-4xl mb-2"></i><p>Your cart is empty</p></div>';
      cartCount.classList.add('hidden');
      cartTotal = 0;
      checkoutBtn.disabled = true;
    } else {
      let itemsHtml = '';
      cartTotal = 0;
      let totalItems = 0;

      cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        cartTotal += itemTotal;
        totalItems += item.quantity;

        const imageHtml = item.image ?
                '<img src="${pageContext.request.contextPath}/' + item.image + '" alt="' + item.title + '" class="w-full h-full object-cover">' :
                '<i class="fas fa-book text-gray-400"></i>';

        itemsHtml += '<div class="flex items-center space-x-3 p-3 border-b border-gray-200">' +
                '<div class="w-12 h-12 bg-gray-200 rounded-md flex items-center justify-center overflow-hidden">' + imageHtml + '</div>' +
                '<div class="flex-1">' +
                '<h4 class="text-sm font-medium text-gray-900 line-clamp-1">' + item.title + '</h4>' +
                '<p class="text-xs text-gray-500">Rs. ' + item.price.toFixed(2) + ' each</p>' +
                '<div class="flex items-center space-x-2 mt-1">' +
                '<button onclick="updateCartItemQuantity(' + item.id + ', ' + (item.quantity - 1) + ')" class="w-6 h-6 bg-gray-200 text-gray-700 rounded text-xs hover:bg-gray-300">' +
                '<i class="fas fa-minus"></i>' +
                '</button>' +
                '<input type="number" id="cart-qty-' + item.id + '" value="' + item.quantity + '" min="1" max="' + item.stock + '" ' +
                'onchange="updateCartItemQuantity(' + item.id + ', parseInt(this.value))" ' +
                'class="w-12 text-center text-xs border border-gray-300 rounded">' +
                '<button onclick="updateCartItemQuantity(' + item.id + ', ' + (item.quantity + 1) + ')" class="w-6 h-6 bg-gray-200 text-gray-700 rounded text-xs hover:bg-gray-300">' +
                '<i class="fas fa-plus"></i>' +
                '</button>' +
                '</div>' +
                '</div>' +
                '<div class="text-right">' +
                '<p class="text-sm font-medium text-gray-900">Rs. ' + itemTotal.toFixed(2) + '</p>' +
                '<button onclick="removeFromCart(' + item.id + ')" class="text-red-500 hover:text-red-700 text-xs mt-1">' +
                '<i class="fas fa-trash"></i>' +
                '</button>' +
                '</div>' +
                '</div>';
      });

      cartItemsContainer.innerHTML = itemsHtml;
      cartCount.textContent = totalItems;
      cartCount.classList.remove('hidden');
      checkoutBtn.disabled = false;
    }

    cartTotalElement.textContent = 'Rs. ' + cartTotal.toFixed(2);
  }

  function toggleCart() {
    const cartSidebar = document.getElementById('cartSidebar');
    const cartOverlay = document.getElementById('cartOverlay');

    if (cartSidebar.classList.contains('translate-x-full')) {
      cartSidebar.classList.remove('translate-x-full');
      cartOverlay.classList.remove('hidden');
    } else {
      cartSidebar.classList.add('translate-x-full');
      cartOverlay.classList.add('hidden');
    }
  }

  function checkout() {
    if (cart.length === 0) {
      alert('Your cart is empty');
      return;
    }

    // Show checkout modal
    showCheckoutModal();
  }

  function showCheckoutModal() {
    const modal = document.getElementById('checkoutModal');
    modal.classList.remove('hidden');

    // Update cart summary in modal
    updateCheckoutSummary();
  }

  function closeCheckoutModal() {
    const modal = document.getElementById('checkoutModal');
    modal.classList.add('hidden');

    // Reset form
    document.getElementById('checkoutForm').reset();
    document.getElementById('customerInfo').classList.add('hidden');
    document.getElementById('paymentSection').classList.add('hidden');
    document.getElementById('verifyBtn').disabled = false;
    document.getElementById('processBtn').disabled = true;
  }

  function updateCheckoutSummary() {
    const summaryContainer = document.getElementById('checkoutSummary');
    let summaryHtml = '';

    cart.forEach(item => {
      const itemTotal = item.price * item.quantity;
      summaryHtml += '<div class="flex justify-between items-center py-2 border-b border-gray-200">' +
              '<div class="flex-1">' +
              '<p class="text-sm font-medium text-gray-900">' + item.title + '</p>' +
              '<p class="text-xs text-gray-500">Qty: ' + item.quantity + ' × Rs. ' + item.price.toFixed(2) + '</p>' +
              '</div>' +
              '<p class="text-sm font-medium text-gray-900">Rs. ' + itemTotal.toFixed(2) + '</p>' +
              '</div>';
    });

    summaryHtml += '<div class="flex justify-between items-center py-3 font-bold text-lg">' +
            '<span>Total:</span>' +
            '<span class="text-green-600">Rs. ' + cartTotal.toFixed(2) + '</span>' +
            '</div>';

    summaryContainer.innerHTML = summaryHtml;
  }

  function verifyCustomer() {
    const phone = document.getElementById('customerPhone').value.trim();

    if (!phone) {
      alert('Please enter customer phone number');
      return;
    }

    // Show loading state
    const verifyBtn = document.getElementById('verifyBtn');
    const originalText = verifyBtn.innerHTML;
    verifyBtn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Verifying...';
    verifyBtn.disabled = true;

    // Send verification request
    fetch('${pageContext.request.contextPath}/checkout/verify-customer', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'phone=' + encodeURIComponent(phone)
    })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        // Customer found, show customer info and payment section
        document.getElementById('customerName').textContent = data.data.name;
        document.getElementById('customerPhoneDisplay').textContent = data.data.phone;
        document.getElementById('customerInfo').classList.remove('hidden');
        document.getElementById('paymentSection').classList.remove('hidden');
        document.getElementById('processBtn').disabled = false;

        // Focus on cash received input
        document.getElementById('cashReceived').focus();

        showNotification('Customer verified successfully!', 'success');
      } else {
        showNotification('There is no registered customer with this number', 'error');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error verifying customer. Please try again.');
    })
    .finally(() => {
      // Reset button state
      verifyBtn.innerHTML = originalText;
      verifyBtn.disabled = false;
    });
  }

  function calculateChange() {
    const totalAmount = cartTotal;
    const cashReceived = parseFloat(document.getElementById('cashReceived').value) || 0;
    const change = cashReceived - totalAmount;

    const changeDisplay = document.getElementById('changeAmount');
    if (cashReceived >= totalAmount) {
      changeDisplay.textContent = 'Rs. ' + change.toFixed(2);
      changeDisplay.className = 'text-lg font-bold text-green-600';
      document.getElementById('processBtn').disabled = false;
    } else {
      changeDisplay.textContent = 'Insufficient amount';
      changeDisplay.className = 'text-lg font-bold text-red-600';
      document.getElementById('processBtn').disabled = true;
    }
  }

  function processCheckout() {
    const phone = document.getElementById('customerPhone').value.trim();
    const cashReceived = parseFloat(document.getElementById('cashReceived').value);

    if (!phone || !cashReceived || cashReceived < cartTotal) {
      alert('Please verify customer and enter valid payment amount');
      return;
    }

    syncCartWithServer();
    setTimeout(() => {
      processCheckoutRequest(phone, cashReceived);
    }, 500);
  }

  function processCheckoutRequest(phone, cashReceived) {
    // Show loading state
    const processBtn = document.getElementById('processBtn');
    const originalText = processBtn.innerHTML;
    processBtn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Processing...';
    processBtn.disabled = true;

    // Send checkout request
    fetch('${pageContext.request.contextPath}/checkout/process', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'customerPhone=' + encodeURIComponent(phone) +
              '&totalAmount=' + cartTotal +
              '&cashReceived=' + cashReceived
    })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                showOrderSuccess(data.data);
                cart = [];
                updateCartDisplay();
                syncCartWithServer();
                closeCheckoutModal();
                toggleCart();

                showNotification('Order completed successfully!', 'success');
              } else {
                alert('Checkout failed: ' + data.message);
              }
            })
            .catch(error => {
              console.error('Error:', error);
              alert('Error processing checkout. Please try again.');
            })
            .finally(() => {
              processBtn.innerHTML = originalText;
              processBtn.disabled = false;
            });
  }

  function showOrderSuccess(orderData) {
    const modal = document.getElementById('orderSuccessModal');

    // Update order details
    document.getElementById('successOrderId').textContent = '#' + orderData.orderId;
    document.getElementById('successCustomerName').textContent = orderData.customerName;
    document.getElementById('successTotalAmount').textContent = 'Rs. ' + orderData.totalAmount.toFixed(2);
    document.getElementById('successCashReceived').textContent = 'Rs. ' + orderData.cashReceived.toFixed(2);
    document.getElementById('successChangeAmount').textContent = 'Rs. ' + orderData.changeAmount.toFixed(2);

    modal.classList.remove('hidden');
  }

  function closeOrderSuccessModal() {
    document.getElementById('orderSuccessModal').classList.add('hidden');
  }

  function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 px-4 py-2 rounded-md text-white z-50 ' +
            (type === 'success' ? 'bg-green-500' : type === 'error' ? 'bg-red-500' : 'bg-blue-500');
    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
      notification.remove();
    }, 3000);
  }

  // Add event listeners for search inputs
  document.getElementById('searchName').addEventListener('input', searchBooks);
  document.getElementById('searchCategory').addEventListener('change', searchBooks);
  document.getElementById('searchISBN').addEventListener('input', searchBooks);
  document.getElementById('searchAuthor').addEventListener('input', searchBooks);

  // Load cart from server when page loads
  document.addEventListener('DOMContentLoaded', function() {
    loadCartFromServer();
  });

  // Initialize total amount display when checkout modal opens
  document.getElementById('checkoutModal').addEventListener('DOMNodeInserted', function() {
    document.getElementById('totalAmountDisplay').textContent = cartTotal.toFixed(2);
  });

  // Close modals when clicking outside
  window.onclick = function(event) {
    const checkoutModal = document.getElementById('checkoutModal');
    const successModal = document.getElementById('orderSuccessModal');

    if (event.target === checkoutModal) {
      closeCheckoutModal();
    }
    if (event.target === successModal) {
      closeOrderSuccessModal();
    }
  }

  // Close modals with Escape key
  document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
      closeCheckoutModal();
      closeOrderSuccessModal();
    }
  });
</script>
</body>
</html>
