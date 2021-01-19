<<<<<<< Updated upstream
=======
DELETE FROM act_id_user WHERE first_ != 'Demo';
-- DELETE FROM act_ru_variable;
-- DELETE FROM act_ru_job;
-- DELETE FROM act_ge_bytearray;
-- DELETE FROM act_re_deployment;

>>>>>>> Stashed changes
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
insert into role (id, name) values (10, 'ROLE_PENDING_AUTHOR');

-- USERS
insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Admin', 'Admin', 'Novi Sad', 'Serbia', 'admin@admin.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);
-- example of disabled user
insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Marko', 'Markovic', 'Novi Sad', 'Serbia', 'marko@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Pera', 'Peric', 'Novi Sad', 'Serbia', 'pera@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Jovan', 'Jovanovic', 'Beograd', 'Serbia', 'jovan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);


insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Ana', 'Antic', 'Kragujevac', 'Serbia', 'ana@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Stefan', 'Stefanovic', 'Kragujevac', 'Serbia', 'stefan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Filip', 'Filipovic', 'Novi Sad', 'Serbia', 'filip@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
<<<<<<< Updated upstream
('Stanko', 'Stankovic', 'Novi Sad', 'Serbia', 'stanko@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);
=======
('Milica', 'Milic', 'Novi Sad', 'Serbia', 'milica@a.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, 0, true, true, true, null);

insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ('9','2', 'Milica', 'Milic', 'milica@a.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
        'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);
>>>>>>> Stashed changes

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Milica', 'Milic', 'Novi Sad', 'Serbia', 'milica@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Ivan', 'Ivanovic', 'Novi Sad', 'Serbia', 'ivan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Milos', 'Milosevic', 'Novi Sad', 'Serbia', 'milos@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Dejan', 'Dejanovic', 'Novi Sad', 'Serbia', 'dejan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Nikola', 'Nikolic', 'Novi Sad', 'Serbia', 'nikola@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);


-- USERS_ROLES
insert into users_roles (user_id, role_id) values (1, 1);

-- LECTORS
insert into users_roles (user_id, role_id) values (2, 5);
insert into users_roles (user_id, role_id) values (3, 5);
insert into users_roles (user_id, role_id) values (4, 5);

-- EDITORS
insert into users_roles (user_id, role_id) values (5, 6);
insert into users_roles (user_id, role_id) values (6, 6);
insert into users_roles (user_id, role_id) values (7, 6);
insert into users_roles (user_id, role_id) values (8, 6);

-- COMMITTEE MEMBERS
insert into users_roles (user_id, role_id) values (9, 8);
insert into users_roles (user_id, role_id) values (10, 8);
insert into users_roles (user_id, role_id) values (11, 8);
insert into users_roles (user_id, role_id) values (12, 8);

-- HEAD OF COMMITTEE
insert into users_roles (user_id, role_id) values (13, 9);
