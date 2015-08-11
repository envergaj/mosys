create database if not exists mosys;
use mosys;

create table employee
(
	id int not null auto_increment,
    employeeNumber varchar(255) not null unique,
    lastName varchar(255) not null,
    firstName varchar(255) not null,
    middleName varchar(255),
    gender varchar(7) not null,
    birthDate date not null,
    civilStatus varchar(7) not null,
    address varchar(1027) not null,
    contactNumber long,
    sssNumber long,
    pagibigNumber long,
    philhealthNumber long,
    tinNumber long,
	cashcardNumber long,
    picture varchar(255),
    deletedFlag boolean not null default false,
    primary key (id)
);

create table coop
(
	id int not null auto_increment,
    coop varchar(255) not null unique,
    coopShortCode varchar(31) not null,
    address varchar(1027) not null,
	sssEmployerNumber long,
    phicEmployerNumber long,
    hdmfEmployerNumber long,
    tinEmployerNumber long,
    deletedFlag boolean not null default false,
    primary key (id)
);

create table client
(
	id int not null auto_increment,
    client varchar(255) not null unique,
    clientShortCode varchar(31) not null,
    deletedFlag boolean not null default false,
    primary key (id)
);

create table workLocation
(
	id int not null auto_increment,
    workLocation varchar(255) not null unique,
    locationShortCode varchar(31) not null,
    deletedFlag boolean not null default false,
    primary key (id)
);

create table position
(
	id int not null auto_increment,
    position varchar(255) not null unique,
    positionShortCode varchar(31) not null,
    deletedFlag boolean not null default false,
    primary key (id)
);

create table employmentHistory
(
	id int not null auto_increment,
    employeeNumber varchar(255) not null,
    position varchar(255) not null,
    startDate date not null,
    workLocation varchar(255) not null,
    coop varchar(255) not null,
    client varchar(255) not null,
    endDate date,
    deletedFlag boolean not null default false,
    primary key (id),
    foreign key (employeeNumber) references employee (employeeNumber)
);

create table deduction
(
	id int not null auto_increment,
    deduction varchar(255) not null,
    deductionShortCode varchar(31) not null unique,
    deletedFlag boolean not null default false,
    primary key (id)
);

create table employeeDeduction
(
	id int not null auto_increment,
    employeeNumber varchar(255) not null,
    employeeName varchar(255) not null,
    principal double not null,
    amortization double not null,
    deductionShortCode varchar(31) not null,
    deletedFlag boolean not null default false,
    primary key (id)
);