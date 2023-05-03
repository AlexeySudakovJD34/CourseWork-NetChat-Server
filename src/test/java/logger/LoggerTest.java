package logger;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoggerTest {
    private Logger logger;

    @Test
    public void loggingWithoutExceptionTest_returnTrue() {
        String msg = "Test message";

        logger = Logger.getInstance();

        boolean actual = logger.log(msg, LogType.MESSAGE);

        assertTrue(actual);
    }
}
