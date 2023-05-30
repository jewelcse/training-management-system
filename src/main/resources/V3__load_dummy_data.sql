INSERT INTO `profiles` (`address1`, `address2`, `city`, `country`, `dob`, `first_name`, `last_name`, `gender`, `state`, `phone`, `street`, `zip_code`)
VALUES ('dummy_profile_address1', 'dummy_profile_address2', 'dummy_profile_city',
        'dummy_profile_country', '2023-05-03', 'dummy_profile_firstname',
        'dummy_profile_lastname', 'male', 'dummy_profile_state', 'phone',
        'dummy_profile_street', '23362');

INSERT INTO `profiles` (`address1`, `address2`, `city`, `country`, `dob`, `first_name`, `last_name`, `gender`, `state`, `phone`, `street`, `zip_code`)
VALUES ('dummy_profile_address1', 'dummy_profile_address2', 'dummy_profile_city',
        'dummy_profile_country', '2023-05-03', 'dummy_profile_firstname',
        'dummy_profile_lastname', 'male', 'dummy_profile_state', 'phone',
        'dummy_profile_street', '23362');

INSERT INTO `users` ( `username`, `email`, `password`, `enabled`, `non_locked`, `profile_id`, `batch_id`)
VALUES ( 'trainee_username', 'trainee_email@gmail.com', '$2a$10$/kYoKkJwgVm.Ouc/vFwvVeD6YtCekxFyvOQRkTJwgQ2iXEoKz2X1q', '0', '0', '2', NULL);

INSERT INTO `users` ( `username`, `email`, `password`, `enabled`, `non_locked`, `profile_id`, `batch_id`)
VALUES ( 'trainer_username', 'trainer_email@gmail.com', '$2a$10$/kYoKkJwgVm.Ouc/vFwvVeD6YtCekxFyvOQRkTJwgQ2iXEoKz2X1q', '0', '0', '2', NULL);

INSERT INTO `courses` (`course_name`, `course_description`, `trainer_id`)
VALUES ( 'dummy_course', 'dummy_course_description', NULL);


INSERT INTO `batches` (`batch_name`, `batch_description`, `start_date`, `end_date`)
VALUES ( 'dummy_batch', 'dummy_batch_description', '2023-05-09', '2023-05-31');

INSERT INTO `batches_courses` (`batch_id`, `course_id`) VALUES ('1', '1');

INSERT INTO `user_verification_center` ( `otp`, `otp_expire_at`, `max_tries`, `user_id`)
VALUES ('akdg4', '2023-05-31 15:32:39', '3', '2');

INSERT INTO `assignments` ( `title`, `total_marks`, `file_location`, `course_id`)
VALUES ('dummy_Assignment', '50', 'kdfjlk/khsdkj/', '1');

INSERT INTO `student_submissions` ( `file_location`, `obtain_marks`, `course_id`, `student_id`, `assignment_id`)
VALUES ( 'dadasd/asdasda/adasd', '30', '1', '2', '1');

INSERT INTO `schedules` (`title`, `description`, `course_start_at`, `course_end_at`, `course_id`, `batch_id`)
VALUES ( 'dummy_scheduling', 'description', '2023-05-15 15:34:27', '2023-05-31 15:34:27', '1', '1');
