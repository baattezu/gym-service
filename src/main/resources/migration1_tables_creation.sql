CREATE TABLE IF NOT EXISTS training_types (
    training_type_id BIGINT PRIMARY KEY,
    training_type_name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    is_active boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS trainee (
    trainee_id BIGINT PRIMARY KEY,
    date_of_birth DATE,
    address varchar(255),
    FOREIGN KEY (trainee_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS trainer (
    trainer_id BIGINT PRIMARY KEY,
    specialization BIGINT,
    FOREIGN KEY (trainer_id) REFERENCES users(user_id),
    FOREIGN KEY (specialization) REFERENCES training_types(training_type_id)
);

CREATE TABLE IF NOT EXISTS trainee_trainer(
    trainee_id BIGINT,
    trainer_id BIGINT,
    PRIMARY KEY(trainee_id, trainer_id),
    FOREIGN KEY (trainee_id) REFERENCES trainee(trainee_id) ON DELETE CASCADE,
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS training(
    training_id BIGSERIAL PRIMARY KEY,
    trainee_id BIGINT,
    trainer_id BIGINT,
    training_name varchar(255) NOT NULL,
    training_type_id BIGINT,
    training_date DATE,
    training_duration BIGINT NOT NULL,
    FOREIGN KEY (trainee_id) REFERENCES trainee(trainee_id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id),
    FOREIGN KEY (training_type_id) REFERENCES training_types(training_type_id)
);

