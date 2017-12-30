create database chat;
use chat;

create table Users(
	username varchar(20) primary key,
    password varchar(20),
    nickname nvarchar(50),
    avatar blob
)

insert into chat.users values ('chi', 'chi', N'Mi Ri Ki', 'C:\Users\Admin\Downloads\Client\res\images\avatars\chi.png');
insert into chat.users values ('dim', 'dim', N'Ngọc Dĩm', 'C:\Users\Admin\Downloads\Client\res\images\avatars\dim.png');
insert into chat.users values ('quang', 'quang', N'Đức Quang', 'C:\Users\Admin\Downloads\Client\res\images\avatars\quang.png');
insert into chat.users values ('thanh', 'thanh', N'Hoài Thanh', 'C:\Users\Admin\Downloads\Client\res\images\avatars\thanh.png');
insert into chat.users values ('thai', 'thai', N'Nguyễn Thành Thái', 'C:\Users\Admin\Downloads\Client\res\images\avatars\thai.png');
