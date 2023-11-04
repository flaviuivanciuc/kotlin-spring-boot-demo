-- Create databases
CREATE DATABASE IF NOT EXISTS `reservations`;

-- Create a new user 'admin' identified by 'admin'
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';

-- Optionally, you can grant privileges to 'admin' from any host
GRANT ALL PRIVILEGES ON `reservations`.* TO 'admin'@'%';
