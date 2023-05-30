CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    non_locked BOOLEAN DEFAULT FALSE,
    profile_id INT NOT NULL,
    batch_id INT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_role (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS profiles(
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    address1 varchar(255) NULL,
    address2 varchar(255) NULL,
    city varchar(255) NULL,
    country varchar(255) NULL,
    dob Date NULL,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NULL,
    gender varchar(50) NULL,
    state varchar(255) NULL,
    phone varchar(11) NULL,
    street varchar(255) NULL,
    zip_code varchar(255) NULL
);

CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    course_description VARCHAR(255),
    trainer_id INT NULL
);

CREATE TABLE IF NOT EXISTS batches (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    batch_name VARCHAR(255) NOT NULL,
    batch_description VARCHAR(255) NULL,
    start_date DATE NULL,
    end_date DATE NULL
);

CREATE TABLE IF NOT EXISTS batches_courses (
    batch_id INT,
    course_id INT,
    PRIMARY KEY (batch_id, course_id)
);

CREATE TABLE IF NOT EXISTS assignments (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title VARCHAR(255) NOT NULL,
    total_marks INT NOT NULL,
    file_location VARCHAR(255),
    course_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS student_submissions(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    file_location VARCHAR(255) NOT NULL,
    obtain_marks double NOT NULL,
    course_id INT NOT NULL,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_verification_center(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    otp VARCHAR(255) NOT NULL,
    otp_expire_at TIMESTAMP NOT NULL,
    max_tries INT NOT NULL,
    user_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS schedules(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    course_start_at timestamp NULL,
    course_end_at timestamp NULL,
    course_id INT NOT NULL,
    batch_id INT NOT NULL
);


ALTER TABLE users
    ADD FOREIGN KEY (profile_id) REFERENCES profiles(id);

ALTER TABLE users
    ADD FOREIGN KEY (batch_id) REFERENCES batches(id);

ALTER TABLE users_role
    ADD FOREIGN KEY (user_id) REFERENCES users(id),
    ADD FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE batches_courses
    ADD FOREIGN KEY (batch_id) REFERENCES batches(id),
    ADD FOREIGN KEY (course_id) REFERENCES courses(id);

ALTER TABLE assignments
    ADD FOREIGN KEY (course_id) REFERENCES courses(id);

ALTER TABLE student_submissions
    ADD FOREIGN KEY (course_id) REFERENCES courses(id),
    ADD FOREIGN KEY (student_id) REFERENCES users(id),
    ADD FOREIGN KEY (assignment_id) REFERENCES assignments(id);

ALTER TABLE user_verification_center
    ADD FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE schedules
    ADD FOREIGN KEY (batch_id) REFERENCES batches(id),
    ADD FOREIGN KEY (course_id) REFERENCES courses(id);




