BEGIN;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(40) UNIQUE NOT NULL,
    password_digest VARCHAR(128) NOT NULL,
    salt VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS dragons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    x DOUBLE PRECISION NOT NULL,
    y REAL NOT NULL,
    creation_date DATE DEFAULT NOW() NOT NULL,
    age INT NOT NULL,
    description TEXT NOT NULL,
    color TEXT NOT NULL CHECK (color = ANY (ARRAY['RED', 'ORANGE', 'WHITE', 'BROWN'])),
    temper TEXT NOT NULL CHECK (temper = ANY (ARRAY['CUNNING', 'EVIL', 'GOOD', 'CHAOTIC_EVIL', 'FICKLE'])),
    cave_depth INT NOT NULL,
    cave_treasures BIGINT NOT NULL,
    creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
    CONSTRAINT chk_age CHECK (age > 0),
    CONSTRAINT chk_y CHECK (y > -948),
    CONSTRAINT chk_cave_treasures CHECK (cave_treasures > 0)
);

END;
