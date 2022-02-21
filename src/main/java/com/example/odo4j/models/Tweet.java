package com.example.odo4j.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Tweet {
    @Id @GeneratedValue
    private long id;
    private String user;
    @Lob private String text;
    private long timestamp;
    @ManyToOne private TwitterCrawlerModel crawler;

    public Tweet() {}

    public Tweet(TwitterCrawlerModel crawler, String user, String text, long timestamp){
        this.crawler = crawler;
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public TwitterCrawlerModel getCrawler() {
        return crawler;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCrawler(TwitterCrawlerModel crawler) {
        this.crawler = crawler;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(this.id, tweet.id)
                && Objects.equals(this.user, tweet.user)
                && Objects.equals(this.text, tweet.text)
                && Objects.equals(this.timestamp, tweet.timestamp);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.user, this.text, this.timestamp);
    }

    @Override
    public String toString(){
        return "Tweet{"
                + "id=" + this.id
                + ", user=@'" +  this.user + '\''
                +", text='" + this.text + '\''
                + ", date" + this.timestamp;
    }
}
