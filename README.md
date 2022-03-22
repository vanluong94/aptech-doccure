# Doccure

Project 4 for Hà Nội Aptech

## Getting started

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