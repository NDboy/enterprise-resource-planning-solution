create table employees
(
    id         varchar(9) not null,
    first_name varchar(30) not null,
    last_name  varchar(30) not null,
    status     varchar(10) not null,
    country    varchar(30) not null,
    zip_code   varchar(20) not null,
    line       varchar(50) not null,
    entry_date date not null,
    primary key (id)
)