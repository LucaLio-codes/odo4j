package util;

import com.example.odo4j.models.TwitterCrawlerModel;
import com.example.odo4j.repos.TweetRepository;

import java.util.HashMap;

public class CrawlerRunner {

    private final HashMap<Long, Thread> map;

    public CrawlerRunner(){
        map = new HashMap<>();
    }

    public void startCrawler(TwitterCrawlerModel crawlerModel, TweetRepository tweetRepository){
        CrawlJob crawler = new CrawlJob(crawlerModel, tweetRepository);
        Thread thread = new Thread(crawler);
        thread.start();
        map.put(crawlerModel.getId(), thread);
    }

    public void interruptCrawler(TwitterCrawlerModel crawler){
        Thread thread = map.remove(crawler.getId());
        thread.interrupt();
        crawler.setRunning(false);
    }
}
