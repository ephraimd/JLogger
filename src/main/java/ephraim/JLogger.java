package ephraim;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * An attempt at a simple logger library for java
 * ************ Example **************
 * import java.io.IOException;
 * import java.nio.file.Paths;
 *
 * public class MainTest {
 *     public static void main(String[] args){
 *         JLogger logger = new JLogger();
 *         logger.fine("JESUS IS LORD");
 *         logger.setLogFilePath(Paths.get("logging.txt"));
 *         logger.info("JESUS IS COMING SOON!!");
 *         logger.warning("JESUS WILL COME!!");
 *         logger.error(new IOException("NOT REPENTING AND TRUSTING GOD IS DANGEROUS!"));
 *         logger.setLogToConsole(false);
 *         logger.info("Lets all gather to Worship now");
 *         logger.warning("THe Time is near");
 *         logger.setLogToConsole(true);
 *         logger.setLogToFile(false);
 *         logger.error(new IOException("THis error is meant only for console"));
 *     }
 * }
 */
public class JLogger {
    /**
     * A Map was used to improve log keeping efficiency
     */
    LinkedList<String> toFileLogBuffer = new LinkedList<String>();
    private boolean logToConsole = false;
    private boolean logToFile = false;
    private Path logFilePath;
    private int fileLogBufferSize = 20;
    private PrintStream consoleStream = System.out;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm.SSS a");

    /**
     * The constructor sets to #logToConsole which instructs the logger to log to console.
     * Using this constructor will not turn on file logging, only console logging
     */
    public JLogger() {
        setup();
        setLogToConsole(true);
    }

    /**
     * The constructor calls #setLogFilePath which sets the Log Path object and instructs the logger to log to file.
     *
     * @param path Path Location of the desired log file. Logger logs in UTF-8
     */
    public JLogger(Path path) {
        setup();
        setLogFilePath(path);
    }

    private void setup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                writeLogsToFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
    }

    public boolean isLogToConsole() {
        return logToConsole;
    }

    public boolean isLogToFile() {
        return logToFile;
    }

    public OutputStream getLogFileOutputStream() {
        try {
            return Files.newOutputStream(logFilePath, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * sets the Log Path object and instructs the logger to log to file.
     * Using this constructor will not turn on console logging, only file logging
     *
     * @param path
     */
    public void setLogFilePath(Path path) {
        this.logFilePath = path;
        try {
            Files.createFile(this.logFilePath);
        } catch (IOException ex) {
        }
        setLogToFile(true);
    }

    /**
     * By default, the Logger will not log to console unless the Logger() constructor variant is called.
     * Use this method to set the Logger to log to console
     *
     * @param logToConsole
     */
    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    /**
     * By default, the Logger will not log to file unless the Logger(Path) constructor variant is called.
     * Use this method to set the Logger to log to file
     *
     * @param logToFile
     */
    public void setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
    }

    /**
     * Set the max number of log entries to buffer before writing to file.
     * The default bufferSize is 20
     *
     * @param fileLogBufferSize Desired value of bufferSize
     */
    public void setFileLogBufferSize(int fileLogBufferSize) {
        this.fileLogBufferSize = fileLogBufferSize;
    }

    /**
     * If you'd prefer not to use the default System.out console stream, then set your preferred console stream with this method
     *
     * @param consoleStream Your preferred console Stream
     */
    public void setConsoleStream(PrintStream consoleStream) {
        this.consoleStream = consoleStream;
    }

    /**
     * logs only throwable objects/exceptions
     *
     * @param err Throwable
     */
    public void error(Throwable err) {
        if(logToConsole){
            err.printStackTrace(consoleStream);
        }
        if(logToFile) {
            println("[ERROR] %s %s", dateFormatter.format(LocalDateTime.now()), throwableToString(err));
        }
    }

    public void fine(String template, Object... args) {
        println(String.format("[FINE] %s %s", dateFormatter.format(LocalDateTime.now()), template), args);
    }

    public void info(String template, Object... args) {
        println(String.format("[INFO] %s %s", dateFormatter.format(LocalDateTime.now()), template), args);
    }

    public void warning(String template, Object... args) {
        println(String.format("[WARNING] %s %s", dateFormatter.format(LocalDateTime.now()), template), args);
    }

    public void println(String template, Object... args) {
        print(template + "\n", args);
    }

    public void print(String template, Object... args) {
        printf(template, args);
    }

    private synchronized void printf(String template, Object... args) {
        if (logToFile) {
            toFileLogBuffer.offerLast(String.format(template, args)); //last in first out
            writeLogBuffer();
        }
        if (logToConsole && !template.contains("[ERROR]")) {
            consoleStream.printf(template, args);
        }
    }

    private void writeLogBuffer() {
        if (toFileLogBuffer.size() >= fileLogBufferSize) {
            try {
                writeLogsToFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                toFileLogBuffer.clear();
            }
        }
    }

    private synchronized void writeLogsToFile() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            for (String entry : toFileLogBuffer) {
                writer.write(entry);
            }
        }
    }

    private String throwableToString(Throwable err) {
        return err.toString();
    }
}
