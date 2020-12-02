-- ROLES
insert into role (id, name) values (1, 'ROLE_ADMIN');
insert into role (id, name) values (2, 'ROLE_READER');
insert into role (id, name) values (3, 'ROLE_BETA_READER');
insert into role (id, name) values (4, 'ROLE_AUTHOR');
insert into role (id, name) values (5, 'ROLE_LECTOR');
insert into role (id, name) values (6, 'ROLE_EDITOR');
insert into role (id, name) values (7, 'ROLE_CHIEF_EDITOR');
insert into role (id, name) values (8, 'ROLE_COMMITTEE_MEMBER');
insert into role (id, name) values (9, 'ROLE_HEAD_OF_COMMITTEE');

-- USERS
insert into user_table (id, first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
(1, 'Admin', 'Admin', 'Novi Sad', 'Serbia', 'admin@admin.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

-- USERS_ROLES
insert into users_roles (user_id, role_id) values (1, 1);