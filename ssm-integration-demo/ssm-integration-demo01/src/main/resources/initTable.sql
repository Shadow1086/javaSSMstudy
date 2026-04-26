CREATE TABLE schedule
(
    id        INT          NOT NULL AUTO_INCREMENT,
    title     VARCHAR(255) NOT NULL,
    completed BOOLEAN      NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO schedule (title, completed)
VALUES ('学习java', TRUE),
       ('学习Python', FALSE),
       ('学习C++', TRUE),
       ('学习JavaScript', FALSE),
       ('学习HTML5', TRUE),
       ('学习CSS3', FALSE),
       ('学习Vue.js', TRUE),
       ('学习React', FALSE),
       ('学习Angular', TRUE),
       ('学习Node.js', FALSE),
       ('学习Express', TRUE),
       ('学习Koa', FALSE),
       ('学习MongoDB', TRUE),
       ('学习MySQL', FALSE),
       ('学习Redis', TRUE),
       ('学习Git', FALSE),
       ('学习Docker', TRUE),
       ('学习Kubernetes', FALSE),
       ('学习AWS', TRUE),
       ('学习Azure', FALSE);
