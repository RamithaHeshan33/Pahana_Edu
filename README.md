<div align='center'>
  
  <img src="https://github.com/user-attachments/assets/94d904d2-3c3a-4c46-a2a4-2ec12a5d9636" height="150px" width="150px" style="border">

  # Pahana Edu - Bookshop Billing System

</div>

A comprehensive web-based billing and inventory management system designed specifically for Pahana Edu Bookshop. This system provides administrators with tools to manage books, categories, customers, and orders efficiently.

## 🚀 Features

- **Dashboard**: Overview of available books with search and filtering capabilities
- **Book Management**: Add, edit, delete, and manage book inventory
- **Category Management**: Organize books into categories
- **Customer Management**: Maintain customer database with contact information
- **Order Processing**: Handle sales transactions with cart functionality
- **Billing System**: Generate receipts and process payments
- **User Authentication**: Secure admin login system

## 🛠️ Technology Stack

### Backend
- **Java** - Programming language
- **Jakarta EE** - Web framework
- **Maven** - Build and dependency management
- **MySQL** - Database

### Frontend
- **JSP (JavaServer Pages)** - Server-side rendering
- **HTML/CSS** - Markup and styling
- **JavaScript** - Client-side functionality
- **Tailwind CSS** - CSS framework
- **Font Awesome** - Icons

### Testing
- **JUnit** - Unit testing framework

## 📁 Project Structure

```
Pahana_Edu/
├── .github/
│   ├── workflows/
│   │   ├── backend.yml          # Backend CI/CD pipeline
│   │   └── frontend.yml         # Frontend validation pipeline
│   └── pull_request_template.md # PR template
├── .idea/                       # IntelliJ IDEA configuration
├── .mvn/                        # Maven wrapper
├── DBScript/
│   └── db.sql                   # Database schema and setup
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/pahana_edu/
│   │   │       ├── business/    # Business logic layer
│   │   │       │   ├── book/    # Book management
│   │   │       │   ├── cart/    # Shopping cart functionality
│   │   │       │   ├── category/# Category management
│   │   │       │   ├── customer/# Customer management
│   │   │       │   ├── dashboard/# Dashboard functionality
│   │   │       │   ├── order/   # Order processing
│   │   │       │   └── user/    # User authentication
│   │   │       ├── persistance/ # Data access layer
│   │   │       │   ├── book/    # Book DAO and models
│   │   │       │   ├── category/# Category DAO and models
│   │   │       │   ├── customer/# Customer DAO and models
│   │   │       │   ├── order/   # Order DAO and models
│   │   │       │   └── user/    # User DAO and models
│   │   │       └── util/        # Utility classes
│   │   ├── resources/
│   │   └── webapp/
│   │       ├── Admin/           # Admin JSP pages
│   │       ├── WEB-INF/
│   │       ├── images/          # Static images
│   │       ├── index.jsp        # Landing page
│   │       ├── login.jsp        # Login page
│   │       ├── register.jsp     # Registration page
│   │       └── sidebar.jsp      # Shared sidebar component
│   └── test/                    # Unit tests
├── target/                      # Build output (generated)
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## 🔧 Prerequisites

Before running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.8+**
- **MySQL 8.0+**
- **Apache Tomcat 8+** (or any Jakarta EE compatible server)
- **Git** (for version control)

## 📋 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/RamithaHeshan33/Pahana_Edu.git
cd Pahana_Edu
```

### 2. Database Setup

1. **Start MySQL Server**
   ```bash
   # On Windows (if MySQL is in PATH)
   net start mysql
   
   # On macOS/Linux
   sudo systemctl start mysql
   # or
   brew services start mysql
   ```

2. **Create Database**
   ```bash
   mysql -u root -p
   ```
   
   Then run the SQL script:
   ```sql
   source DBScript/db.sql
   ```
   
   Or manually execute the contents of `DBScript/db.sql` in your MySQL client.

3. **Update Database Configuration**
   
   Edit the database connection settings in:
   `src/main/java/org/example/pahana_edu/util/DBConn.java`
   
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/pahana_edu";
   private static final String USER = "your_mysql_username";
   private static final String PASSWORD = "your_mysql_password";
   ```


### 3. Build the Project

```bash
# Navigate to project directory
cd Pahana_Edu

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package
```

### 4. Deploy to Tomcat

1. **Copy WAR file to Tomcat**
   ```bash
   cp target/Pahana_Edu-1.0-SNAPSHOT.war /path/to/tomcat/webapps/
   ```

2. **Start Tomcat**
   ```bash
   # Navigate to Tomcat bin directory
   cd /path/to/tomcat/bin
   
   # On Windows
   startup.bat
   
   # On macOS/Linux
   ./startup.sh
   ```

3. **Access the Application**
   
   Open your browser and navigate to:
   ```
   http://localhost:8080/Pahana_Edu-1.0-SNAPSHOT/
   ```

## 🚀 Running the Project

### Development Mode

For development, you can use your IDE's built-in server or Maven plugins:

```bash
# Using Maven Tomcat plugin (if configured)
mvn tomcat7:run

# Or deploy to local Tomcat instance
mvn clean package
# Then copy target/*.war to Tomcat webapps/
```

### Production Deployment

1. **Build for production**
   ```bash
   mvn clean package -Dmaven.test.skip=true
   ```

2. **Deploy WAR file**
   - Copy the generated WAR file from `target/` to your production Tomcat's `webapps/` directory
   - Ensure database connection settings are configured for production
   - Update email settings if needed

## 🔐 Default Access

### First Time Setup

1. **Register an Administrator**
   - Navigate to the registration page
   - Create your first admin account

2. **Login**
   - Use your registered credentials to access the admin dashboard

### Sample Data

The database script includes the basic table structure. You can add sample data by:

1. **Adding Categories**
   - Go to Categories → Add categories like "Fiction", "Non-Fiction", "Educational", etc.

2. **Adding Customers**
   - Go to Customers → Add customer information for billing

3. **Adding Books**
   - Go to Books → Add your book inventory with details

## 🧪 Testing

### Run Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BookServiceTest

```

### Test Coverage

The project includes comprehensive unit tests for:
- Service layer components
- Business logic validation
- Data access operations

## 🔄 CI/CD Pipeline

The project includes GitHub Actions workflows:

### Backend CI (`/.github/workflows/backend.yml`)
- Runs on push/PR to main/dev branches
- Tests with MySQL database
- Builds and packages the application
- Runs security scans
- Uploads build artifacts

### Frontend Validation (`/.github/workflows/frontend.yml`)
- Validates HTML/CSS/JavaScript in JSP files
- Checks for accessibility issues
- Performs security checks
- Generates validation reports

## 📝 Development Workflow

### Branch Strategy
- `main` - Production-ready code
- `dev` - Development branch
- `qa` - Quality assurance testing
- `regression` - Pre-production testing

### Pull Request Process
1. Create feature branch from `dev`
2. Implement changes with tests
3. Create PR using the provided template
4. Code review and CI checks
5. Merge to target branch

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is proprietary software developed for Pahana Edu Bookshop.

---

<div align='center'>

  <img width="300" height="100" alt="image" src="https://github.com/user-attachments/assets/a233f0a1-37aa-4b80-87c1-88b801c86ccb" />

</div>



