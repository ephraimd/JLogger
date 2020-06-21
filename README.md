# JLogger
An attempt at creating a pretty simple but blazing fast logger utility in Java. An attempt though

### Code
```java
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
        logger.warning("The Time is near");
        logger.setLogToConsole(true);
        logger.setLogToFile(false);
        logger.error(new IOException("This error is meant only for console"));
    }
}
```
There are methods to check logging output type
```
logger.isLogToConsole();
logger.isLogToFile();
```
To redirect console logging to another stream
```
logger.setConsoleStream(new PrintStream(new FileOutputStream(consoleLoggingFile.toFile())));
```

### Internals
JLogger's approach is to minimize the bottleneck involved in keeping logs to O(1) - bare minimum; while keeping the library as fast as possible
The measures taken to ensure this are
- Logs are kept in a ___LinkedList___ buffer and only written when the buffer reaches a predefined size. The size of this buffer can be specified with ___JLogger#setFileLogBufferSize___ method.
- JLogger attaches a Hook to the JVM's shutdown routine that empties the log buffer into the file at the end. This primarily designed for log writing accuracy.

JLogger's decision to write to file or console is controlled by the ___JLogger#setLogToFile___ and ___setLogToConsole___ respectively. 
By default, JLogger logs to console. When ___JLogger#setLogFilePath___ is called, ___JLogger#setLogToFile___ is called implicitly when enables file logging but does not turn off console logging.
So, control must be specified by the programmer as to how JLogger should output logs. 
 
### Usage
Please checkout the release page for the latest artifact jar or simply clone the repo as a gradle project