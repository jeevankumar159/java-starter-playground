// File: Logger.java
public class Logger {

    
    
    private Logger() {}


    public static Logger getInstance() {

    }

    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
