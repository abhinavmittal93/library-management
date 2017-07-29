create table Lms_Login(user_id varchar2(10) constraint lms_login_user_pk primary key,password varchar2(15) NOT NULL,role varchar2(15) NOT NULL);

insert into lms_login values('admin','admin','admin');
insert into lms_login values('user','user','user');
ALTER TABLE lms_login ADD verification varchar2(1);
ALTER TABLE lms_login ADD email_id varchar2(45);
insert into lms_login values('student','student','student','N','abhinavmittal93@gmail.com');
select * from lms_login;
Desc Lms_Login

create sequence isbn_seq
increment by 1
start with 1001
nomaxvalue
nocache
nocycle
order;


create table lms_isbn(isbn_no varchar2(10) constraint lms_isbn_no_pk primary key,category varchar2(30));
DESC LMS_ISBN

create sequence bookid_seq
increment by 1
start with 101
nomaxvalue
nocycle
nocache
order;


create table lms_books(isbn_no varchar2(10) constraint lms_books_isbn_fk references lms_isbn(isbn_no),book_id varchar2(20) constraint lms_books_bookid_pk primary key,book_title varchar2(250) not null,book_author varchar2(250) not null,status varchar2(15) not null,book_price number not null);
DESC LMS_BOOKS

create sequence reg_seq
increment by 1
start with 1
nomaxvalue
nocycle
nocycle
nocache
order;

create table lms_mails(msg_id varchar2(10) constraint mails_pk primary key,per_id varchar2(10) constraint mail_per_fk references lms_login(user_id),content varchar2(4000) not null);
DESC LMS_MAILS

create sequence jor_seq
increment by 1
start with 1
nomaxvalue
nocycle
nocache
order;

create table lms_journals(journal_id varchar2(5) constraint lms_jor_pk primary Key,name varchar2(100) Not Null,price varchar2(10) not null,period varchar2(20) not null,issueon varchar2(20) not null);
DESC lms_journals

drop table lms_books_issue;
create table lms_books_issue(return_date DATE,stu_id varchar2(20) constraint student_id_fk references Lms_Login(user_id), book_id varchar2(20) constraint book_id_fk references lms_books(book_id) )
DESC lms_books_issue

COMMIT

select * from lms_login;
select * from lms_books;
select * from lms_mails ;
select * from lms_isbn ;
select * from lms_books_issue ;

insert into lms_books values(01,103,"Oracle","Kathy","Available",200)

select role from lms_login where user_id='student' and password='student'

insert into lms_books_issue values(sysdate, 'student', '102' )

insert into lms_isbn values('01','01')
insert into lms_isbn values('02','02')



