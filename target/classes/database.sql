DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
	fio VARCHAR(50) NOT NULL,
	login VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL
) ENGINE=INNODB CHARACTER SET utf8 COLLATE utf8_unicode_ci;



CREATE TABLE IF NOT EXISTS books
(
	book_id INT PRIMARY KEY AUTO_INCREMENT,
	user_id INT,
	titleBook VARCHAR(100) NOT NULL,
	createDate DATETIME,
	INDEX book_user (user_id),
	CONSTRAINT books_users_fk
	FOREIGN KEY (user_id)  REFERENCES users (user_id)
	ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB CHARACTER SET utf8 COLLATE utf8_unicode_ci;



INSERT INTO users(user_id, fio, login, password)
VALUES
(1, 'Tom Smith', 'tom', '123'),
(NULL, 'Jery Bad', 'jery', '123'),
(NULL, 'Kati Lin', 'kati', '123'),
(NULL, 'Sid Nice', 'sid', '123');

INSERT INTO books(book_id, user_id, titleBook, createDate)
VALUES
(1, 1, 'Книга Иова', '2001-07-11'),
(NULL, 2, 'Отец Горио', '2002-02-21'),
(NULL, 3, 'Грозовой перевал', '2005-10-01'),
(NULL, 4, 'Голод', '2015-06-14'),
(NULL, 2, 'Голод', '2017-01-10'),
(NULL, 4, 'Фауст', '2018-06-24'),
(NULL, 3, 'Фауст', '2013-09-04'),
(NULL, 1,'Жак-фаталист и его хозяин', '2010-11-08'),
(NULL, 1, 'Медея', '2008-09-19');

