INSERT INTO training_types(training_type_id, training_type_name)
VALUES (1, 'Cardio'),
       (2, 'Explosive'),
       (3, 'Boxing');

INSERT INTO users(first_name, last_name, username, password, is_active)
VALUES ('John', 'Dorian', 'John.Dorian', '3shdasdho1', true),
       ('Bob', 'Kelso', 'Bob.Kelso', 'fes34affd!', true),
       ('Elliot', 'Reid', 'Elliot.Reid', '!fds%32dax',true),
       ('Percival', 'Cox', 'Percival.Cox', 'a54#fdsfds', true);

INSERT INTO trainee (trainee_id, date_of_birth, address)
VALUES
          ( (SELECT user_id from users WHERE username='John.Dorian') ,'10-09-1986', 'Baker St.21'    ),
          ( (SELECT user_id from users WHERE username='Bob.Kelso') ,'01-12-1950', 'AlBukerke St.123' );

INSERT INTO trainer (trainer_id, specialization)
VALUES
    ( (SELECT user_id from users WHERE username='Elliot.Reid') , (SELECT training_type_id from training_types WHERE training_type_name='Cardio')),
    ( (SELECT user_id from users WHERE username='Percival.Cox') ,(SELECT training_type_id from training_types WHERE training_type_name='Boxing'));

INSERT INTO trainee_trainer (trainee_id, trainer_id)
VALUES  ((SELECT user_id from users WHERE username='John.Dorian'), (SELECT user_id from users WHERE username='Elliot.Reid')),
        ((SELECT user_id from users WHERE username='Bob.Kelso'),(SELECT user_id from users WHERE username='Percival.Cox'));

INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (
        (SELECT user_id from users WHERE username='John.Dorian'),
        (SELECT user_id from users WHERE username='Elliot.Reid'),
        'Heavy Cardio', (SELECT training_type_id from training_types WHERE training_type_name='Boxing'),
        '11-09-2024', 130
       );
