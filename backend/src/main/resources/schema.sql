-- User Table
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(255),
    gender INT DEFAULT 0, -- 0:未知, 1:男, 2:女
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Exercise Table
CREATE TABLE exercise (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    is_big_three BOOLEAN DEFAULT FALSE, -- 是否属于三大项
    user_id BIGINT DEFAULT 0 -- 0为系统预设，其他为用户自定义
);

-- Workout Session Table
CREATE TABLE workout_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    total_volume DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Workout Activity Table
CREATE TABLE workout_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    order_num INT,
    FOREIGN KEY (session_id) REFERENCES workout_session(id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(id)
);

-- Workout Set Table
CREATE TABLE workout_set (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT NOT NULL,
    set_index INT,
    weight DECIMAL(10, 2),
    reps INT,
    rpe DECIMAL(3, 1),
    FOREIGN KEY (activity_id) REFERENCES workout_activity(id)
);

-- Weight Log Table
CREATE TABLE weight_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    weight DECIMAL(5, 2),
    log_date DATE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Supplement Log Table
CREATE TABLE supplement_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    name VARCHAR(255),
    dosage VARCHAR(255),
    taken_time TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES workout_session(id)
);
