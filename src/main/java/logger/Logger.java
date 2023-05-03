package logger;

import java.io.*;
import java.text.*;
import java.util.Date;

public class Logger {
    private final File logFile;
    private static Logger logger;
    private PrintWriter out;


    private Logger() {
        logFile = new File("log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException exp) {
                System.out.println("Fail to create log file");
            }
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    // TODO boolean заменить на void
    public synchronized boolean log(String message, LogType type) {
        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            out = new PrintWriter(bw);
            message = getCurrentTime() + message;
            switch (type) {
                case MESSAGE -> out.println("[MESSAGE]" + message);
                case INFO -> out.println("[INFO]" + message);
                case ERROR -> out.println("[ERROR]" + message);
            }
            return true;
        } catch (IOException exp) {
            System.out.println("Cannot get access to log file");
            return false;
        } finally {
            out.close();
        }
    }

    public static String getCurrentTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
        String date = format.format(new Date());
        return "[" + date + "] ";
    }
}
