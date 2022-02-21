package com.example.odo4j.models;



import com.example.odo4j.controllers.TweetController;
import twitter4j.*;
import twitter4j.Query;

import javax.persistence.*;
import java.util.List;

@Entity
public class TwitterCrawlerModel {

    @Id @GeneratedValue long id;
    String searchQuery;

    public TwitterCrawlerModel(){
    }

    public TwitterCrawlerModel(String query){
        this.searchQuery = query;
    }

    public long getId() {
        return id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }


    public void setId(long id) {
        this.id = id;
    }


    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }


}

