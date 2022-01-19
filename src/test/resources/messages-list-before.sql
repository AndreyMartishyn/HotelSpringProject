delete from message;

insert into message (id, text,tag, user_id) values
(1, "first message", "first", 1),
(2, "second message", "second", 1),
(3, "third message", "third", 1),
(4, "fourth message with tag#1", "first", 1);

ALTER TABLE message AUTO_INCREMENT = 10;
