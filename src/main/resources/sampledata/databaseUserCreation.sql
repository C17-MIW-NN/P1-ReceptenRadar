USE `receptenradar`;
CREATE USER IF NOT EXISTS 'receptUser'@'localhost' IDENTIFIED BY 'receptUserPW';
GRANT ALTER, CREATE, DELETE, DROP, INSERT, REFERENCES, SELECT, UPDATE ON receptenradar . * TO 'receptUser'@'localhost';
