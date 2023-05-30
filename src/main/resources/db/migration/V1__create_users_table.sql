CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    non_locked BOOLEAN DEFAULT TRUE,
    profile_id INT,
    FOREIGN KEY (profile_id) REFERENCES profiles(id)
);
