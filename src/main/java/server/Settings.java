package server;

import logger.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Settings {
    private String hostName;
    private int port;
    private final Logger logger = Logger.getInstance();

    public String getHostName() {
        return hostName;
    }
    public int getPort() {
        return port;
    }

    public Settings() {
        JSONParser parser = new JSONParser();
        try
        {
            Object obj = parser.parse(new FileReader("src/main/resources/settings.json"));
            JSONObject jsonObj = (JSONObject) obj;
            this.hostName = (String) jsonObj.get("host");
            this.port = Integer.parseInt((String) jsonObj.get("port"));
        } catch (IOException | ParseException | ClassCastException exp) {
            String eMessage = "Cannot get access to settings file";
            System.out.println(eMessage);
            logger.log(eMessage, LogType.ERROR);
        }
    }
}
