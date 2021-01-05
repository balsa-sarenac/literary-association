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
('Ana', 'Antic', 'Kragujevac', 'Serbia', 'nina.sarenac@hotmail.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Stefan', 'Stefanovic', 'Kragujevac', 'Serbia', 'nina.sarenac@hotmail.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Filip', 'Filipovic', 'Novi Sad', 'Serbia', 'nina.sarenac@hotmail.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Stanko', 'Stankovic', 'Novi Sad', 'Serbia', 'nina.sarenac@hotmail.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 false, true, true, true, null);
 -- 9
insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Milica', 'Milic', 'Novi Sad', 'Serbia', 'milica@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ('9','2', 'Milica', 'Milic', 'milica@example.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
        'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Ivan', 'Ivanovic', 'Novi Sad', 'Serbia', 'ivan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ('10','3', 'Ivan', 'Ivanovic', 'ivan@example.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
        'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Milos', 'Milosevic', 'Novi Sad', 'Serbia', 'milos@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

-- insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ('milos@example.com','4', 'Milos', 'Milosevic', 'milos@example.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
--         'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Dejan', 'Dejanovic', 'Novi Sad', 'Serbia', 'dejan@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

-- insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ('dejan@example.com','5', 'Dejan', 'Dejanovic', 'dejan@example.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
--         'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Nikola', 'Nikolic', 'Novi Sad', 'Serbia', 'nikola@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);

-- insert into ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, LOCK_EXP_TIME_, ATTEMPTS_, PICTURE_ID_) values ( 'nikola@example.com', '6', 'Nikola', 'Nikolic',  'nikola@example.com', '{SHA-512}DdwyJqhGQFMQ46VdZIMX0xjnuR9Yfta+k2NqLkpYuoE/ENfm7i13D14SVcBikXzTnXBY1mP38+R098mtj3ZErQ==',
--         'Cc6LwfxJ+9rBrq/kf8paBg==', null, null, null);

insert into user_table (first_name, last_name, city, country, email, password, enabled,
                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
('Milan', 'Milanovic', 'Novi Sad', 'Serbia', 'nina.sarenac444@gmail.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
 true, true, true, true, null);


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

-- AUTHOR
insert into users_roles (user_id, role_id) values (14, 4);

--GENRES
insert into genre (id, name) values (1,'Action');
insert into genre (id, name) values (2,'Adventure');
insert into genre (id, name) values (3,'Comic Book');
insert into genre (id, name) values (4,'Fantasy');
insert into genre (id, name) values (5,'Mystery');
insert into genre (id, name) values (6,'Historical Fiction');
insert into genre (id, name) values (7,'Horror');
insert into genre (id, name) values (8,'Science Fiction');
insert into genre (id, name) values (9,'Short Story');
insert into genre (id, name) values (10,'Thriller');


--insert into user_table (first_name, last_name, city, country, email, password, enabled,
--                        account_non_expired, account_non_locked, credentials_non_expired, last_password_reset_date) values
--('Sima', 'Simic', 'Novi Sad', 'Serbia', 'sima@example.com', '$2a$10$N4CZptDrasoEx3IJHL.3ZO1q8xICGMf.EBQY98m.PiR6RjHExRENK',
-- true, true, true, true, null);

--insert into users_roles (user_id, role_id) values (1, 7);

--insert into book(id, title, chief_editor_id) values (1, 'book1', 1);
--insert into authors_books(author_id, book_id) values (14, 1);

--insert into publishing_request(approved, book_id) values (false, 1);