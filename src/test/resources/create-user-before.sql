delete from user_role;
delete from users;

insert into users (id, active, password, username) values
(1, true,'$2a$10$URwJ7gr.4tH3wpzOTy.20eK1EhqlQsEfPBiUcnmQ/DcZVNubPUzZK' , 'admin'),
(2, true, '$2a$10$LJq21k20fZpPhSfs0c1KDOiKOUaLdTrrXtRTkiwtsd7xQOJvjvIMG', 'user1');

insert into user_role (user_id, roles) values
(1, 'ADMIN'),(1, 'USER'),
(2, 'USER');





