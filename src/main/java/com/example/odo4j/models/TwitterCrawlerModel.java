package com.example.odo4j.models;



import com.example.odo4j.controllers.TweetController;
import twitter4j.*;
import twitter4j.Query;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
public class TwitterCrawlerModel {

    @Id @GeneratedValue long id;
    String searchQuery;
    String since;
    String until;

    public TwitterCrawlerModel(){
    }

    public TwitterCrawlerModel(String searchQuery){
        this.searchQuery = searchQuery;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.since = LocalDate.now().minusMonths(1).format(formatter);
        this.until = LocalDate.now().format(formatter);
    }

    public TwitterCrawlerModel(String query, String since, String until){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            formatter.parse(since);
            formatter.parse(until);
            this.since = since;
            this.until = until;
        }catch (DateTimeParseException e){
            // Fall back to default values
            this.since = LocalDate.now().minusMonths(1).format(formatter);
            this.until = LocalDate.now().format(formatter);
        }finally {
            this.searchQuery = query;
        }
    }

    public long getId() {
        return id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public String getSince() {
        return since;
    }

    public String getUntil(){
        return until;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}

