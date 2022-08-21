INSERT INTO `roles` (`id`, `name`)
VALUES (NULL, 'ROLE_TRAINEE'),
       (NULL, 'ROLE_TRAINER'),
       (NULL, 'ROLE_ADMIN');


INSERT INTO `users` (`id`,  `email`, `password`, `username`,`enabled`,`non_locked`)
VALUES (NULL, 'admin@gmail.com', '$2a$10$bWjl8AP9XR/x42ZM40RiuOSsevm5PrfjMIAupERS.Ya6EgtLQU3oa', 'admin','1','1');


INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('1', '3');