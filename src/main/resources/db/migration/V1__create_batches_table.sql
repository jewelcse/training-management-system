CREATE TABLE batches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    batch_name VARCHAR(255) NOT NULL,
    batch_description VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);