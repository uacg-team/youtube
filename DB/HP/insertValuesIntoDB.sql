SELECT * FROM youtubedb.users;
SELECT * FROM youtubedb.videos;
SELECT * FROM youtubedb.videoprivacy_settingss;
SELECT * FROM youtubedb.privacy_settings;

ALTER TABLE videos AUTO_INCREMENT = 1;

SELECT * FROM videos WHERE privacy_id = 1 ORDER BY date DESC;

SELECT v.location_url FROM videos as v join videos_has_tags as vt on(v.video_id = vt.video_id) join tags as t on (vt.tag_id = t.tag_id) where t.tag = 'one' LIMIT 5;

-- privacy settings
INSERT into privacy_settings (name) values('Public');
INSERT into privacy_settings (name) values('Private');

-- users 5
-- password is Hristo
INSERT INTO users (username, password, email, date_creation, avatar_url, gender) VALUES ('Hristo1', '85be8c7b3b9066b27d9e9147d3c87ef0f304c7b705384b3daac178072dd1982e', 'hristo1@penev.bg','2017-10-11', 'defaultAvatar.png', '');
INSERT INTO users (username, password, email, date_creation, avatar_url, gender) VALUES ('Hristo2', '85be8c7b3b9066b27d9e9147d3c87ef0f304c7b705384b3daac178072dd1982e', 'hristo2@penev.bg','2017-10-11', 'defaultAvatar.png', '');
INSERT INTO users (username, password, email, date_creation, avatar_url, gender) VALUES ('Hristo3', '85be8c7b3b9066b27d9e9147d3c87ef0f304c7b705384b3daac178072dd1982e', 'hristo3@penev.bg','2017-10-11', 'defaultAvatar.png', '');
INSERT INTO users (username, password, email, date_creation, avatar_url, gender) VALUES ('Hristo4', '85be8c7b3b9066b27d9e9147d3c87ef0f304c7b705384b3daac178072dd1982e', 'hristo4@penev.bg','2017-10-11', 'defaultAvatar.png', '');
INSERT INTO users (username, password, email, date_creation, avatar_url, gender) VALUES ('Hristo5', '85be8c7b3b9066b27d9e9147d3c87ef0f304c7b705384b3daac178072dd1982e', 'hristo5@penev.bg','2017-10-11', 'defaultAvatar.png', '');
-- videos 25
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name1','2017-10-11 00:00:01','1.mp4',1,1,0,'description1');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name2','2017-10-11 00:00:02','2.mp4',1,1,0,'description2');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name3','2017-10-11 00:00:03','3.mp4',1,1,0,'description3');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name4','2017-10-11 00:00:04','4.mp4',1,2,0,'description4');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name5','2017-10-11 00:00:05','5.mp4',1,2,0,'description5');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name6','2017-10-11 00:00:06','6.mp4',2,1,0,'description6');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name7','2017-10-11 00:00:07','7.mp4',2,1,0,'description7');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name8','2017-10-11 00:00:08','8.mp4',2,1,0,'description8');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name9','2017-10-11 00:00:09','9.mp4',2,2,0,'description9');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name10','2017-10-11 00:00:10','10.mp4',2,2,0,'description10');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name11','2017-10-11 00:00:11','11.mp4',3,1,0,'description11');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name12','2017-10-11 00:00:12','12.mp4',3,1,0,'description12');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name13','2017-10-11 00:00:13','13.mp4',3,1,0,'description13');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name14','2017-10-11 00:00:14','14.mp4',3,2,0,'description14');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name15','2017-10-11 00:00:15','15.mp4',3,2,0,'description15');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name16','2017-10-11 00:00:16','16.mp4',4,1,0,'description16');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name17','2017-10-11 00:00:17','17.mp4',4,1,0,'description17');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name18','2017-10-11 00:00:18','18.mp4',4,1,0,'description18');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name19','2017-10-11 00:00:19','19.mp4',4,2,0,'description19');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name20','2017-10-11 00:00:20','20.mp4',4,2,0,'description20');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name21','2017-10-11 00:00:21','21.mp4',5,1,0,'description21');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name22','2017-10-11 00:00:22','22.mp4',5,1,0,'description22');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name23','2017-10-11 00:00:23','23.mp4',5,1,0,'description23');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name24','2017-10-11 00:00:24','24.mp4',5,2,0,'description24');
INSERT INTO videos (name,date, location_url,user_id,privacy_id,views,description) VALUES('name25','2017-10-11 00:00:25','25.mp4',5,2,0,'description25');
-- users_follow_users
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('1', '2');
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('1', '3');
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('1', '4');
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('3', '2');
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('2', '1');
INSERT INTO `youtubedb`.`users_follow_users` (`user_id`, `follower_id`) VALUES ('5', '1');

-- likes
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (1, 1, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (1, 2, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (1, 3, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (1, 4, 1);

INSERT INTO video_likes (video_id, user_id, isLike) VALUES (2, 1, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (2, 2, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (2, 3, 1);
INSERT INTO video_likes (video_id, user_id, isLike) VALUES (2, 4, 1);

-- playlist
INSERT INTO playlists (playlist_name,user_id) VALUES ('name',1);

-- playlists_has_videos
INSERT INTO playlists_has_videos (playlist_id,video_id) VALUES (1,1);
INSERT INTO playlists_has_videos (playlist_id,video_id) VALUES (1,2);
INSERT INTO playlists_has_videos (playlist_id,video_id) VALUES (1,3);

-- tags
INSERT INTO tags (tag_id,tag) VALUES (1,'one');
INSERT INTO tags (tag_id,tag) VALUES (2,'two');
INSERT INTO tags (tag_id,tag) VALUES (3,'three');
INSERT INTO tags (tag_id,tag) VALUES (4,'four');


-- videos_has_tags
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (1,1);
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (2,1);
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (3,1);
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (4,2);
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (5,2);
INSERT INTO videos_has_tags (video_id,tag_id) VALUES (6,2);