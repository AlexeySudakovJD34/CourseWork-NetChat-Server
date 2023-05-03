package server;

import logger.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final Logger logger = Logger.getInstance();
    private ServerSocket serverSocket;
    private List<MessageProcessor> clientsConnected = new CopyOnWriteArrayList<>();
    private Settings settings = new Settings();


    public void startServer() {
        int port = settings.getPort();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exp) {
            logger.log("Fail to start server", LogType.INFO);
        }
        String start = "SuChat Server has being started";
        System.out.println(start);
        logger.log(start, LogType.INFO);
    }
    public void waitingForConnection() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // сервер ждет подключения

                // при подключении создаем новый поток, который будет обрабатывать сообщения от клиента
                new Thread(new MessageProcessor(clientSocket, clientsConnected)).start();
            } catch (IOException exp) {
                logger.log("Unexpected server error", LogType.ERROR);
                break;
            }
        }
    }
}