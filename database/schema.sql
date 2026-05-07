create database if not exists meditrak;
use meditrak;

create table if not exists hospitals (
    id bigint primary key auto_increment,
    name varchar(120) not null,
    zone varchar(40) not null,
    contact_person varchar(100) not null,
    email varchar(120) not null,
    phone varchar(30) not null,
    address varchar(255) not null
);

create table if not exists waste_entries (
    id bigint primary key auto_increment,
    hospital_id bigint not null,
    waste_type varchar(80) not null,
    quantity_kg decimal(10,2) not null,
    bin_color varchar(30) not null,
    collection_status varchar(40) not null,
    pickup_date date not null,
    constraint fk_waste_hospital foreign key (hospital_id) references hospitals(id)
);

create table if not exists routes (
    id bigint primary key auto_increment,
    zone varchar(40) not null,
    vehicle_number varchar(30) not null,
    driver_name varchar(100) not null,
    estimated_waste_kg decimal(10,2) not null,
    optimized_distance_km decimal(10,2) not null
);
