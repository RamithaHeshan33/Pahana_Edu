<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login - Pahana Edu</title>
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center">
<div class="max-w-md w-full mx-4">
  <!-- Logo/Header -->
  <div class="text-center mb-8">
    <div class="inline-flex items-center justify-center w-16 h-16 bg-indigo-600 rounded-full mb-4">
      <i class="fas fa-graduation-cap text-white text-2xl"></i>
    </div>
    <h1 class="text-3xl font-bold text-gray-900">Pahana Edu</h1>
    <p class="text-gray-600 mt-2">Sign in to your account</p>
  </div>

  <!-- Login Form -->
  <div class="bg-white rounded-xl shadow-lg p-8">
    <% if (request.getAttribute("error") != null) { %>
    <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
      <div class="flex items-center">
        <i class="fas fa-exclamation-circle mr-2"></i>
        <%= request.getAttribute("error") %>
      </div>
    </div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-6">
      <div class="flex items-center">
        <i class="fas fa-check-circle mr-2"></i>
        <%= request.getAttribute("success") %>
      </div>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/auth/login" method="post" class="space-y-6">
      <div>
        <label for="username" class="block text-sm font-medium text-gray-700 mb-2">
          <i class="fas fa-user mr-2"></i>Username
        </label>
        <input type="text"
               id="username"
               name="username"
               required
               class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200"
               placeholder="Enter your username">
      </div>

      <div>
        <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
          <i class="fas fa-lock mr-2"></i>Password
        </label>
        <div class="relative">
          <input type="password"
                 id="password"
                 name="password"
                 required
                 class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200"
                 placeholder="Enter your password">
          <button type="button"
                  onclick="togglePassword()"
                  class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-gray-700">
            <i id="passwordIcon" class="fas fa-eye"></i>
          </button>
        </div>
      </div>

      <button type="submit"
              class="w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition duration-200 font-medium">
        <i class="fas fa-sign-in-alt mr-2"></i>Sign In
      </button>
    </form>

    <div class="mt-6 text-center">
      <p class="text-gray-600">
        Don't have an account?
        <a href="${pageContext.request.contextPath}/auth/register"
           class="text-indigo-600 hover:text-indigo-700 font-medium">
          Sign up here
        </a>
      </p>
    </div>
  </div>

  <!-- Footer -->
  <div class="text-center mt-8 text-gray-500 text-sm">
    <p>&copy; 2025 Pahana Edu. All rights reserved.</p>
  </div>
</div>

<script>
  function togglePassword() {
    const passwordInput = document.getElementById('password');
    const passwordIcon = document.getElementById('passwordIcon');

    if (passwordInput.type === 'password') {
      passwordInput.type = 'text';
      passwordIcon.className = 'fas fa-eye-slash';
    } else {
      passwordInput.type = 'password';
      passwordIcon.className = 'fas fa-eye';
    }
  }

  // Auto-hide alerts after 5 seconds
  setTimeout(() => {
    const alerts = document.querySelectorAll('.bg-red-50, .bg-green-50');
    alerts.forEach(alert => {
      alert.style.transition = 'opacity 0.5s';
      alert.style.opacity = '0';
      setTimeout(() => alert.remove(), 500);
    });
  }, 5000);
</script>
</body>
</html>