CREATE TABLE author
(
    id      INTEGER PRIMARY KEY,
    name    TEXT    NOT NULL,
    age     INTEGER NOT NULL,
);

CREATE TABLE mp3
(
    id      INTEGER PRIMARY KEY,
    author_id      INTEGER NOT NULL,
    name    TEXT    NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE VIEW mp3_view AS
SELECT a.name AS author_name, a.id as author_id, m.name as mp3_name, m.id as mp3_id from mp3 m inner join author a on m.author_id = a.id;
