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
                RateLimitStatus rateLimit = result.getRateLimitStatus();
                if (rateLimit != null){
                    int remaining = rateLimit.getRemaining();
                    int resetTime = rateLimit.getSecondsUntilReset();
                    int sleep = 0;
                    if (remaining == 0) {
                        sleep = resetTime + 1; //adding 1 more seconds
                    } else {
                        sleep = (resetTime / remaining) + 1; //adding 1 more seconds
                    }
                    Thread.sleep(Math.max(sleep * 1000, 0));
                }

            } while ((query = result.nextQuery()) != null && !Thread.currentThread().isInterrupted());
        } catch (TwitterException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            twitterCrawlerModel.setRunning(false);
        }
    }
}
