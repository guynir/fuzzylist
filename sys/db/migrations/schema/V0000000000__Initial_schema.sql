--
-- 'list' table.
--
CREATE TABLE IF NOT EXISTS list (
    id            BIGSERIAL PRIMARY KEY,
    key           VARCHAR(200) NOT NULL,
    left_to_right BOOLEAN      NOT NULL,
    title         VARCHAR(500) NOT NULL
    );

ALTER TABLE list OWNER TO postgres;

--
-- 'list_text' table.
--
CREATE TABLE IF NOT EXISTS list_text (
    id        BIGSERIAL PRIMARY KEY,
    index     INTEGER       NOT NULL,
    text      VARCHAR(1024) NOT NULL,
    parent_id BIGINT        NOT NULL CONSTRAINT fkdav4aqofk5i6tuk88mnrkgveg REFERENCES list
    );
ALTER TABLE list_text ADD CONSTRAINT UKncy37klhkevr6n53n2shvoj4u UNIQUE (parent_id, index);
ALTER TABLE list_text OWNER TO postgres;

