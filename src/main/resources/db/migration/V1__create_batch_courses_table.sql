CREATE TABLE batch_course (
    batch_id INT,
    course_id INT,
    FOREIGN KEY (batch_id) REFERENCES batches(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    PRIMARY KEY (batch_id, course_id)
);