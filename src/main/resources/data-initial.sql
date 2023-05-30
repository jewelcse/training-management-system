
INSERT INTO `roles` (`id`, `name`)
VALUES (NULL, 'ROLE_TRAINEE'),
       (NULL, 'ROLE_TRAINER'),
       (NULL, 'ROLE_ADMIN');

INSERT INTO `profiles`(`id`, `address1`, `address2`, `city`, `country`, `dob`, `first_name`, `gender`, `last_name`, `phone_number`, `state`, `street`, `zip_code`)
VALUES (NULL,'value-2','value-3','value-4','value-5',null,'value-7','value-8','value-9','value-10','value-11','value-12','value-13');

INSERT INTO `users` (`id`,  `email`, `password`, `username`,`enabled`,`non_locked`,`profile_id`)
VALUES (NULL, 'admin@gmail.com', '$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa', 'admin',true,true,'1');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('1', '3');
