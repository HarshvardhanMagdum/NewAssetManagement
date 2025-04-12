DROP DATABASE IF EXISTS digital_asset_management;
CREATE DATABASE digital_asset_management;
USE digital_asset_management;


CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);


CREATE TABLE assets (
    asset_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    purchase_date DATE NOT NULL,
    location VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES employees(employee_id)
);


CREATE TABLE maintenance_records (
    maintenance_id INT AUTO_INCREMENT PRIMARY KEY,
    asset_id INT,
    maintenance_date DATE NOT NULL,
    description TEXT NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (asset_id) REFERENCES assets(asset_id)
);


CREATE TABLE asset_allocations (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    asset_id INT,
    employee_id INT,
    allocation_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (asset_id) REFERENCES assets(asset_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);


CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    asset_id INT,
    employee_id INT,
    reservation_date DATE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (asset_id) REFERENCES assets(asset_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

INSERT INTO employees (employee_id, name, department, email, password) VALUES
(101, 'Ravi Sharma', 'IT', 'ravi.sharma@example.com', 'ravi123'),
(102, 'Priya Singh', 'Finance', 'priya.singh@example.com', 'priya123'),
(103, 'Amit Patel', 'Operations', 'amit.patel@example.com', 'amit123'),
(104, 'Sneha Desai', 'HR', 'sneha.desai@example.com', 'sneha123'),
(105, 'Karan Mehta', 'Admin', 'karan.mehta@example.com', 'karan123');

INSERT INTO assets (asset_id, name, type, serial_number, purchase_date, location, status, owner_id)
VALUES
  (1, 'Dell Latitude 7440', 'Laptop', 'DL7420', '2022-03-15', 'Mumbai', 'under maintenance', 101),
  (2, 'Mahindra Scorpio N', 'Vehicle', 'SCORPIO-N-7788', '2021-09-10', 'Delhi Garage', 'in use', 102),
  (3, 'Canon Projector X300', 'Equipment', 'CNPX300-5566', '2020-06-22', 'Training Room - Mumbai', 'decommissioned', 103);

INSERT INTO maintenance (asset_id, maintenance_date, description, cost)
VALUES
  (2, '2025-04-01', 'replaced brake pads', 10000.00);
  
  INSERT INTO reservation (
    asset_id, employee_id, reservation_date, start_date, end_date, status
) VALUES (
    3, 105, '2025-04-10', '2025-04-12', '2025-04-18', 'pending'
);


