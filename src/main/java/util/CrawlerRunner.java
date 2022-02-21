package util;

import com.example.odo4j.models.TwitterCrawlerModel;
import com.example.odo4j.repos.TweetRepository;

import java.util.HashMap;

public class CrawlerRunner {

    private final HashMap<TwitterCrawlerModel, Thread> map;

    public CrawlerRunner(){
        map = new HashMap<>();
    }

    public void startCrawler(TwitterCrawlerModel crawlerModel, TweetRepository tweetRepository){
        CrawlJob crawler = new CrawlJob(crawlerModel, tweetRepository);
        Thread thread = new Thread(crawler);
        thread.start();
        map.put(crawlerModel, thread);
    }

    public void interruptCrawler(TwitterCrawlerModel crawler){
        System.out.println(map.toString());
        Thread thread = map.remove(crawler);
        thread.interrupt();
    }
}
