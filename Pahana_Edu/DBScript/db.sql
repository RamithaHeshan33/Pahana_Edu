-- Create DB
CREATE DATABASE IF NOT EXISTS pahana_edu;

-- Use DB
USE pahana_edu;

-- Create users table
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

-- Show table structure
DESCRIBE admins;
