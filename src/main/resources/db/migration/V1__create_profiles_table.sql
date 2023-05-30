CREATE TABLE profiles(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    address1 varchar(255) NOT NULL,
    address2 varchar(255) NOT NULL,
    city varchar(255) NOT NULL,
    country varchar(255) NOT NULL,
    dob Date NULL,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    gender varchar(50) NOT NULL,
    state varchar(255) NOT NULL,
    phone varchar(11) NOT NULL,
    street varchar(255) NOT NULL,
    zip_code varchar(255) NOT NULL
);