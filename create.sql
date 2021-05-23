create table accounts
(
    id             bigserial      not null,
    balance        numeric(19, 2) not null,
    iban           varchar(255)   not null,
    name           varchar(255)   not null,
    status         varchar(255)   not null,
    client_id      int8           not null,
    credit_card_id int8,
    primary key (id)
)
create table clients
(
    id      bigserial    not null,
    status  varchar(255) not null,
    user_id int8,
    primary key (id)
)
create table credit_cards
(
    id          bigserial    not null,
    card_number varchar(255) not null,
    is_expired  boolean      not null,
    account_id  int8,
    primary key (id)
)
create table payments
(
    id            bigserial      not null,
    amount        numeric(19, 2) not null,
    details       varchar(255),
    payment_date  date           not null,
    receiver_iban varchar(255)   not null,
    status        varchar(255)   not null,
    account_id    int8           not null,
    primary key (id)
)
create table unblock_requests
(
    id            bigserial not null,
    action_date   timestamp,
    creation_date timestamp not null,
    account_id    int8      not null,
    client_id     int8      not null,
    primary key (id)
)
create table users
(
    id        bigserial    not null,
    email     varchar(255) not null,
    is_admin  boolean      not null,
    name      varchar(255) not null,
    password  varchar(255) not null,
    client_id int8,
    primary key (id)
)
create
index ACCOUNT_NAME_INDEX on accounts (name)
create
index PAYMENT_BALANCE_INDEX on accounts (balance)
alter table credit_cards
    add constraint CARD_NUMBER_UNIQUE_CONSTRAINT unique (card_number)
create
index PAYMENT_DATE_INDEX on payments (payment_date)
create
index PAYMENT_ACCOUNT_INDEX on payments (account_id)
alter table users
    add constraint EMAIL_UNIQUE_CONSTRAINT unique (email)
alter table accounts
    add constraint FK_CLIENT foreign key (client_id) references clients
alter table accounts
    add constraint FK_CREDIT_CARD foreign key (credit_card_id) references credit_cards
alter table clients
    add constraint FK_USER_ID foreign key (user_id) references users
alter table credit_cards
    add constraint FK_ACCOUNT_ID foreign key (account_id) references accounts
alter table payments
    add constraint FK_PAYMENT_ACCOUNT foreign key (account_id) references accounts
alter table unblock_requests
    add constraint FK_UNBLOCK_REQUEST_ACCOUNT foreign key (account_id) references accounts
alter table unblock_requests
    add constraint FK_UNBLOCK_REQUEST_CLIENT foreign key (client_id) references clients
alter table users
    add constraint FK_USER_CLIENT_ID foreign key (client_id) references clients
