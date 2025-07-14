<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen">

<!-- Navigation -->
<nav class="bg-white shadow-sm">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
            <div class="flex items-center">
                <div class="w-8 h-8 bg-indigo-600 rounded-lg flex items-center justify-center mr-3">
                    <i class="fas fa-graduation-cap text-white"></i>
                </div>
                <h1 class="text-xl font-bold text-gray-900">Pahana Edu</h1>
            </div>
            <div class="flex space-x-4">
                <a href="${pageContext.request.contextPath}/auth/register"
                   class="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition duration-200">
                    Get Started
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
    <div class="text-center">
        <div class="inline-flex items-center justify-center w-20 h-20 bg-indigo-600 rounded-full mb-8">
            <i class="fas fa-graduation-cap text-white text-3xl"></i>
        </div>
        <h1 class="text-5xl font-bold text-gray-900 mb-6">
            Welcome to <span class="text-indigo-600">Pahana Edu</span>
        </h1>
        <p class="text-xl text-gray-600 mb-8 max-w-3xl mx-auto">
            Your gateway to quality education. Join thousands of learners who are advancing their skills
            and achieving their goals with our comprehensive learning management system.
        </p>
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <a href="${pageContext.request.contextPath}/auth/register"
               class="bg-indigo-600 text-white px-8 py-4 rounded-lg hover:bg-indigo-700 transition duration-200 font-medium text-lg">
                <i class="fas fa-rocket mr-2"></i>Start Learning Today
            </a>
            <a href="login.jsp"
               class="border border-indigo-600 text-indigo-600 px-8 py-4 rounded-lg hover:bg-indigo-50 transition duration-200 font-medium text-lg">
                <i class="fas fa-sign-in-alt mr-2"></i>Sign In
            </a>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="bg-gray-900 text-white py-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2025 Pahana Edu. | All rights reserved.</p>
        </div>
    </div>
</footer>
</body>
</html>