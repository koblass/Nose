drop table user if exists;
create table user (
  id int not null primary key,
  first_name varchar(30) not null,
  last_name  varchar(30) not null,
  age int,
  birth date,
  address_id int
);

drop table user_access if exists;
create table user_access (
  user_id int not null,
  last_access TIMESTAMP not null,
  PRIMARY KEY (user_id, last_access)
);

drop table personal_address if exists;
create table personal_address (
  id int not null primary key,
  street varchar(250) not null,
  zip  varchar(10) not null,
  city varchar(50),
  country_code varchar(2) not null
);

drop table country if exists;
create table country (
  code varchar(2) not null,
  language varchar(5) not null,
  name varchar(50) not null,
  PRIMARY KEY (code, language)
);

drop table invoice if exists;
create table invoice (
  id int not null primary key,
  user_id int not null,
  date TIMESTAMP not null
);

drop table invoice_item if exists;
create table invoice_item (
  id int not null primary key,
  invoice_id int not null,
  quantity int not null,
  name varchar(50) not null,
  description varchar(250) not null,
  price DECIMAL not null
);