INSERT INTO users(email, name, password, is_admin)
VALUES ('olegvynnyk@gmail.com', 'Oleg Vynnyk', '1111', true),
       ('justinbieber@gmail.com', 'Justin Bieber', '1111', false),
       ('britneyspears@gmail.com', 'Britney Spears', '1111', false);

INSERT INTO clients(user_id, status)
VALUES (2, 'ACTIVE'),
       (3, 'ACTIVE');

UPDATE users SET client_id = 1 WHERE id = 2;
UPDATE users SET client_id = 2 WHERE id = 3;

INSERT INTO accounts(id, client_id, name, balance, status, iban)
VALUES (1, 1, 'Salary', 2500, 'ACTIVE', 'UA2532820912340000056789'),
       (2, 2, 'Sugar Daddy', 40000, 'ACTIVE', 'UA2532820112340010056781'),
       (3, 2, 'Salary', 15223, 'ACTIVE', 'UA2592820912940010999781');

INSERT INTO credit_cards(card_number, account_id, is_expired)
VALUES
       ('0000111122223333', 1, false),
       ('1111222233334444', 2, false),
       ('2222333344445555', 2, false);

UPDATE accounts
SET credit_card_id = 1
WHERE id = 1;

UPDATE accounts
SET credit_card_id = 2
WHERE id = 2;

UPDATE accounts
SET credit_card_id = 3
WHERE id = 3;

INSERT INTO payments (account_id, amount, status, receiver_iban, payment_date, details)
VALUES
       (2, 1000, 'SENT', 'UA2532820112340010056781', '2021-03-12', 'Cat food'),
       (2, 2499, 'SENT', 'UA2532820112340010056781', '2021-04-19', ''),
       (3, 500, 'SENT', 'UA2512320112341230056123', '2021-04-19', 'Burger King'),
       (2, 23, 'SENT', 'UA2532820112340010056781', '2021-05-01', ''),
       (2, 150, 'SENT', 'UA2532820112340010056781', '2021-05-15', ''),
       (3, 349, 'SENT', 'UA2532820112340010051111', '2021-05-15', 'McDonalds'),
       (2, 349, 'SENT', 'UA2532820112340010056781', '2021-05-15', 'aaaaaaaaaaa');