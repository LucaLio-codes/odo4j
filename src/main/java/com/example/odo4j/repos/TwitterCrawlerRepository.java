package com.example.odo4j.repos;


import com.example.odo4j.models.TwitterCrawlerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterCrawlerRepository extends JpaRepository<TwitterCrawlerModel, Long> {
}
