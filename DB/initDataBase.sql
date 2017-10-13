Drop SCHEMA youtubedb;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema youtubeDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema youtubeDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `youtubeDB` DEFAULT CHARACTER SET utf8 ;
USE `youtubeDB` ;

-- -----------------------------------------------------
-- Table `youtubeDB`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT 'username fro account\n',
  `password` VARCHAR(50) NOT NULL,
  `facebook` VARCHAR(50) NULL,
  `email` VARCHAR(50) NOT NULL,
  `date_creation` DATETIME NOT NULL,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`playlists`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`playlists` (
  `playlist_id` INT NOT NULL AUTO_INCREMENT,
  `playlist_name` VARCHAR(45) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`playlist_id`, `user_id`),
  INDEX `fk_playlists_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_playlists_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`privacy_settings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`privacy_settings` (
  `privacy_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`privacy_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`videos` (
  `video_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `views` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `location_url` VARCHAR(200) NOT NULL,
  `user_id` INT NOT NULL,
  `thumbnail_url` VARCHAR(200) NULL,
  `description` VARCHAR(1000) NULL,
  `privacy_id` INT NOT NULL,
  PRIMARY KEY (`video_id`),
  INDEX `fk_videos_users1_idx` (`user_id` ASC),
  INDEX `fk_videos_privacy_settings1_idx` (`privacy_id` ASC),
  CONSTRAINT `fk_videos_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_videos_privacy_settings1`
    FOREIGN KEY (`privacy_id`)
    REFERENCES `youtubeDB`.`privacy_settings` (`privacy_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`comments` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(200) NULL,
  `date` DATETIME NOT NULL,
  `video_id` INT NOT NULL,
  `replay_id` INT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_comments_videos1_idx` (`video_id` ASC),
  INDEX `fk_comments_comments1_idx` (`replay_id` ASC),
  INDEX `fk_comments_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_comments_videos1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtubeDB`.`videos` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_comments1`
    FOREIGN KEY (`replay_id`)
    REFERENCES `youtubeDB`.`comments` (`comment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`playlists_has_videos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`playlists_has_videos` (
  `playlist_id` INT NOT NULL,
  `video_id` INT NOT NULL,
  PRIMARY KEY (`playlist_id`, `video_id`),
  INDEX `fk_playlists_has_videos_videos1_idx` (`video_id` ASC),
  INDEX `fk_playlists_has_videos_playlists1_idx` (`playlist_id` ASC),
  CONSTRAINT `fk_playlists_has_videos_playlists1`
    FOREIGN KEY (`playlist_id`)
    REFERENCES `youtubeDB`.`playlists` (`playlist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_playlists_has_videos_videos1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtubeDB`.`videos` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`tags` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `tag` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `tag_UNIQUE` (`tag` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`videos_has_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`videos_has_tags` (
  `video_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`video_id`, `tag_id`),
  INDEX `fk_videos_has_tags_tags1_idx` (`tag_id` ASC),
  INDEX `fk_videos_has_tags_videos1_idx` (`video_id` ASC),
  CONSTRAINT `fk_videos_has_tags_videos1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtubeDB`.`videos` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_videos_has_tags_tags1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `youtubeDB`.`tags` (`tag_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`comments_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`comments_likes` (
  `user_id` INT NOT NULL,
  `comment_id` INT NOT NULL,
  `isLike` TINYINT NULL,
  PRIMARY KEY (`user_id`, `comment_id`),
  INDEX `fk_users_has_comments_comments1_idx` (`comment_id` ASC),
  INDEX `fk_users_has_comments_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_users_has_comments_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_comments_comments1`
    FOREIGN KEY (`comment_id`)
    REFERENCES `youtubeDB`.`comments` (`comment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`users_follow_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`users_follow_users` (
  `user_id` INT NOT NULL,
  `follower_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `follower_id`),
  INDEX `fk_users_has_users_users2_idx` (`follower_id` ASC),
  INDEX `fk_users_has_users_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_users_has_users_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_users_users2`
    FOREIGN KEY (`follower_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtubeDB`.`video_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtubeDB`.`video_likes` (
  `video_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `isLike` TINYINT NULL,
  PRIMARY KEY (`video_id`, `user_id`),
  INDEX `fk_videos_has_users_users1_idx` (`user_id` ASC),
  INDEX `fk_videos_has_users_videos1_idx` (`video_id` ASC),
  CONSTRAINT `fk_videos_has_users_videos1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtubeDB`.`videos` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_videos_has_users_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `youtubeDB`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT into privacy_settings (name) values('Private');
INSERT into privacy_settings (name) values('Public');

INSERT INTO users (username, password, email, date_creation) VALUES ('Hristo', 'Penev', 'hristo@penev.bg','2017-10-11');
INSERT INTO users (username, password, email, date_creation) VALUES ('Velichko', 'Angelov', 'vel@angelov.bg','2017-10-11');

INSERT into videos (name,date, location_url,user_id,privacy_id,views) VALUES('name1','2017-10-11','www.somewhere1.com',1,1,0);
INSERT into videos (name,date, location_url,user_id,privacy_id,views) VALUES('name2','2017-10-11','www.somewhere2.com',2,2,0);

INSERT into playlists (playlist_name,user_id) VALUES('playlist1',1);
INSERT into playlists (playlist_name,user_id) VALUES('playlist2',2);

INSERT into tags (tag) VALUES('tag1');
INSERT into tags (tag) VALUES('tag2');

INSERT into comments (text,date,video_id,user_id) VALUES('comment1','2017-10-11',1,1);
INSERT into comments (text,date,video_id,user_id) VALUES('comment2','2017-10-11',2,2);

INSERT into comments_likes (user_id,comment_id,isLike) VALUES(1,1,1);
INSERT into comments_likes (user_id,comment_id,isLike) VALUES(2,2,0);