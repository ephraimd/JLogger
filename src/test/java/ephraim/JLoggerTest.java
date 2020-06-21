package ephraim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JLoggerTest {

    @Test
    void isWorking() throws IOException {
        Path loggingFile = Paths.get("logging.txt");
        Path consoleLoggingFile = Paths.get("simulated_console.txt");
        Files.deleteIfExists(loggingFile);
        Files.deleteIfExists(consoleLoggingFile);
        Files.createFile(consoleLoggingFile);

        JLogger logger = new JLogger();
        logger.setConsoleStream(new PrintStream(new FileOutputStream(consoleLoggingFile.toFile()))); //sets console logging to a file print
        logger.fine("JESUS IS LORD"); //c
        logger.setLogFilePath(loggingFile);
        logger.info("JESUS IS COMING SOON!!"); //cf
        logger.warning("JESUS WILL COME!!"); //cf
        logger.setLogToConsole(false);
        logger.info("Lets all gather to Worship now"); //f
        logger.warning("The Time is near"); //f

        Assertions.assertTrue(Files.exists(loggingFile));
        Assertions.assertTrue(Files.exists(consoleLoggingFile));

        /* //todo: fix the Assertions
        List<String> logList = new ArrayList<>(10);
        List<String> consoleList = new ArrayList<>(10);
        List<String> expectedConsoleList = Arrays.asList("JESUS IS LORD", "JESUS IS COMING SOON!!", "JESUS WILL COME!!");
        List<String> expectedFileList = Arrays.asList("JESUS IS COMING SOON!!", "JESUS WILL COME!!",
                "Lets all gather to Worship now", "The Time is near");

        System.out.println(Files.readAllLines(loggingFile));
        Assertions.assertDoesNotThrow(() -> logList.addAll(Files.readAllLines(loggingFile).stream()
                .filter(log -> !log.trim().isEmpty())
                .collect(Collectors.toList())), "JLogger did not create the logging file");
        Assertions.assertDoesNotThrow(() -> consoleList.addAll(Files.readAllLines(consoleLoggingFile).stream()
                .filter(log -> !log.trim().isEmpty())
                .collect(Collectors.toList())), "JLogger did not write to console properly");
        Assertions.assertLinesMatch(expectedFileList, logList, "File Logs data are not as expected");
        Assertions.assertLinesMatch(expectedConsoleList, consoleList, "Console Logs data are not as expected");
        */
    }
}