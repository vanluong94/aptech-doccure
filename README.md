# Doccure

Project 4 for Hà Nội Aptech

## Getting started

- Cài đặt Eclipse và JDK 1.8 từ Oracle
- Cài đặt Lombok: https://projectlombok.org/download
- Cài đặt Spring Tools 4 for Eclipse (STS)
- Cài đặt Maven

```
use doccure;
create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);
```

## Skipping Tests with Maven 
`mvn -DskipTests package`