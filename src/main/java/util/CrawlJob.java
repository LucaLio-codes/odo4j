package util;

import com.example.odo4j.models.Tweet;
import com.example.odo4j.models.TwitterCrawlerModel;
import com.example.odo4j.repos.TweetRepository;
import twitter4j.*;

import java.util.List;

public class CrawlJob implements Runnable{

    private final TwitterCrawlerModel twitterCrawlerModel;
    private final TweetRepository tweetRepository;

    public CrawlJob(TwitterCrawlerModel twitterCrawlerModel, TweetRepository repository){
        this.tweetRepository = repository;
        this.twitterCrawlerModel = twitterCrawlerModel;
    }

    @Override
    public void run () {
        twitterCrawlerModel.setRunning(true);
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query(twitterCrawlerModel.getSearchQuery());
            query.setCount(100);
            query.setSince(twitterCrawlerModel.getSince());
            query.setUntil(twitterCrawlerModel.getUntil());
            QueryResult result;
            int i = 0;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if (!tweet.isRetweet()) {
                        tweetRepository.save(new Tweet(twitterCrawlerModel, tweet.getUser().getScreenName(), tweet.getText(), tweet.getCreatedAt().getTime()));
                    }
                }
                if (i == 99) {
                    Thread.sleep(100);
                    i = 0;
                }else{
                    i++;
                }

            } while ((query = result.nextQuery()) != null && !Thread.currentThread().isInterrupted());
        } catch (TwitterException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            twitterCrawlerModel.setRunning(false);
        }
    }
}
