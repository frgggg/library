create table books (id int8 not null, back_time timestamp, name varchar(200) not null, primary key (id));
create table books_reader (reader_id int8, book_id int8 not null, primary key (book_id));
create table books_writers (book_id int8 not null, writer_id int8 not null);
create table readers (id int8 not null, is_debtor boolean not null, name varchar(100) not null, primary key (id));
create table writers (id int8 not null, comment varchar(100), first_name varchar(100) not null, middle_name varchar(100), surname varchar(100) not null, primary key (id));

alter table if exists writers add constraint UKnaxubo38fg93phjhvgd9toi7i unique (first_name, surname, middle_name, comment);

create sequence hibernate_sequence start 1 increment 1;

alter table if exists books_reader add constraint FKo5rh7fowdsbicwuhuyccovg55 foreign key (reader_id) references readers;
alter table if exists books_reader add constraint FK2g50kre8hg8oy1a3v6mkdw3t foreign key (book_id) references books;
alter table if exists books_writers add constraint FKt3u2vdjmd25510aii68qshl2j foreign key (writer_id) references writers;
alter table if exists books_writers add constraint FKay1r7fvj70dj8s798rwygkocl foreign key (book_id) references books;