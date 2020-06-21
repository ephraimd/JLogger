import ephraim.JLogger;

import java.io.IOException;
import java.nio.file.Paths;

public class MainTest {
    public static void main(String[] args){
        JLogger logger = new JLogger();
        logger.fine("JESUS IS LORD");
        logger.setLogFilePath(Paths.get("logging.txt"));
        logger.info("JESUS IS COMING SOON!!");
        logger.warning("JESUS WILL COME!!");
        logger.error(new IOException("NOT REPENTING AND TRUSTING GOD IS DANGEROUS!"));
        logger.setLogToConsole(false);
        logger.info("Lets all gather to Worship now");
        logger.warning("THe Time is near");
        logger.setLogToConsole(true);
        logger.setLogToFile(false);
        logger.error(new IOException("THis error is meant only for console"));
    }
}
