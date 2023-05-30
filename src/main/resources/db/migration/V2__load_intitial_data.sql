INSERT INTO `roles` (`role_name`)
VALUES ('ROLE_TRAINEE'),
       ('ROLE_TRAINER'),
       ('ROLE_ADMIN');


INSERT INTO `profiles` (`address1`, `address2`, `city`, `country`, `dob`, `first_name`, `last_name`, `gender`, `state`, `phone`, `street`, `zip_code`)
VALUES ('varchar', 'varchar', 'varchar', 'varchar', '2023-05-16', 'varchar', 'varchar', 'varchar', 'varchar', 'varchar', 'varchar', 'varchar');


INSERT INTO `users` (`email`, `password`, `username`,`enabled`,`non_locked`,`profile_id`)
VALUES ('admin@gmail.com', '$2a$10$/kYoKkJwgVm.Ouc/vFwvVeD6YtCekxFyvOQRkTJwgQ2iXEoKz2X1q', 'admin',true,true,1);

INSERT INTO `users_role` (`user_id`, `role_id`) VALUES (1,3);


