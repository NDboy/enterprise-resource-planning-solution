create table partners
(
    id       varchar(8) not null,
    name     varchar(50) unique,
    country  varchar(30),
    zip_code varchar(10),
    line     varchar(50),
    tax_no   varchar(20) unique,
    primary key (id)
);

create table `partner_ibans`
(
            partner_id varchar(255) not null,
            ibans      varchar(255) unique
);

ALTER TABLE `partner_ibans`
    ADD CONSTRAINT `FK_partner_ibans_partners`
        FOREIGN KEY (`partner_id`)
            REFERENCES `partners` (`id`);