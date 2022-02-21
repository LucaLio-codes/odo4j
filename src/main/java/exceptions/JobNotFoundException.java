package exceptions;

public class JobNotFoundException extends RuntimeException{
    public JobNotFoundException(long id){
        super("could not find job" + id);
    }
}
