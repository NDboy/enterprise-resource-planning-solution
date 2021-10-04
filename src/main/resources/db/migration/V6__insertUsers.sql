insert into users(username, password, enabled) values ('user4', '$2a$10$phn5uVEiHZYpmAN.fNAZwe2JF4of8.4CERV6AJgFYa5xTDwQt/7Mq', true);
insert into users(username, password, enabled) values ('Csanad', '$2a$10$6BooWwg9liopPocsnTgyZOBz5vQX79vIQaJIrxMY9f5eeQzMDS7Qy', true);
insert into authorities(username, authority) values ('user4', 'normal_user');
insert into authorities(username, authority) values ('Csanad', 'normal_user');