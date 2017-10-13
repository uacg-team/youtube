SELECT * FROM youtubedb.users;
SELECT * FROM youtubedb.videos;
SELECT * FROM youtubedb.privacy_settings;

DELETE FROM users;
DELETE FROM videos;

INSERT into privacy_settings (name) values('Private');
INSERT into privacy_settings (name) values('Public');

INSERT INTO users (username, password, email, date_creation) VALUES ('Hristo', 'Penev', 'hristo@penev.bg','2017-10-11');

INSERT into videos (name,date, location_url,user_id,privacy_id,views) VALUES('name','2017-10-11','www.somewhere.com',1,2,0);

SELECT username, password FROM users WHERE username = 'Hristo';

SELECT COUNT(*) as number FROM users WHERE username = 'Hristo';






