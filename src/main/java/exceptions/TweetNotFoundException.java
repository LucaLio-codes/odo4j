package exceptions;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(long id){
        super("could not find tweet  " + id);
    }
}
