USE inventory;

-- Create the Branch table
CREATE TABLE IF NOT EXISTS Branch (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL
    );

-- Create the User (Employee) table with a foreign key to Branch
CREATE TABLE IF NOT EXISTS User (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    lastname VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    branch_id VARCHAR(50) NULL,
    FOREIGN KEY (branch_id) REFERENCES Branch(id) ON DELETE CASCADE
    );

-- Create the Product table with a foreign key to Branch
CREATE TABLE IF NOT EXISTS Product (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    inventory_stock INT NOT NULL,
    price FLOAT NOT NULL,
    category VARCHAR(100) NOT NULL,
    branch_id VARCHAR(50),
    FOREIGN KEY (branch_id) REFERENCES Branch(id) ON DELETE CASCADE
    );

INSERT INTO User (id, name, lastname, password, email, role, branch_id)
SELECT 'b7bd872a-e42b-43d7-a8fa-399a234619fc', 'admin', 'admin', '$2a$10$fqVlgWVxYJwUWs9FDqkm1.dYGSwq698.JFusqfScLZ2e81zLFxiOu', 'admin@admin.com', 'SUPER', null
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM User WHERE email = 'admin@admin.com');
