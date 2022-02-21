package com.example.odo4j.repos;

import com.example.odo4j.models.Tweet;
import com.example.odo4j.models.TwitterCrawlerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findAllByCrawler(TwitterCrawlerModel crawler);
}
