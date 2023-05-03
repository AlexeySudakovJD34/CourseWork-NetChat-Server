package server;

import logger.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class MessageProcessor implements Runnable{
    private final Socket clientSocket;
    private PrintWriter outcome;
    private BufferedReader income;
    private String userName;
    private List<MessageProcessor> clientsConnected;
    private final Logger logger = Logger.getInstance();

    public MessageProcessor(Socket clientSocket, List<MessageProcessor> clientsConnected) {
        this.clientSocket = clientSocket;
        this.clientsConnected = clientsConnected;
    }
    public PrintWriter getOutcome() {
        return outcome;
    }
    public void setOutcome(PrintWriter outcome) {
        this.outcome = outcome;
    }
    public String getUserName() {
        return userName;
    }
    public void setUsername(String userName) {
        this.userName = userName;
    }
    public void setIncome(BufferedReader income) {
        this.income = income;
    }

    @Override
    public void run() {
        try {
            outcome = new PrintWriter(clientSocket.getOutputStream(), true);
            income = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            regService();
            while (readAndSendMessage());
        } catch (IOException exp) {
            clientsConnected.remove(this);
            String info = userName + " has disconnected";
            printMsgInConsoleAndSendToAll(info);
            logger.log(info, LogType.INFO);
        } finally {
            outcome.close();
            try {
                income.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }
    public boolean regService() {
        outcome.println("Enter your nickname");
        String nickName;
        try {
            do {
                nickName = income.readLine();
            } while (isNicknameExists(nickName));
            userName = nickName;
            String info = userName + " has joined SuChat";
            printMsgInConsoleAndSendToAll(info);
            logger.log(info, LogType.INFO);
            outcome.println("Welcome to SuChat, " + userName + ", please enter your messages in console:");
            clientsConnected.add(this);
            return true;
        } catch (IOException exp) {
            System.out.println("User has disconnected");
            return false;
        }
    }
    public boolean isNicknameExists(String nickName) {
        for (MessageProcessor client : clientsConnected) {
            if (client.getUserName().equals(nickName)) {
                outcome.println("This nickname is already taken, please chose another one");
                return true;
            }
        }
        return false;
    }
    public boolean readAndSendMessage() throws IOException {
        String message = income.readLine();
        if (message.equalsIgnoreCase("/exit")) {
            String info = userName + " has left SuChat";
            printMsgInConsoleAndSendToAll(info);
            clientsConnected.remove(this);
            logger.log(info, LogType.INFO);
            return false;
        } else if (message != null) {
            printMsgInConsoleAndSendToAll(userName + " said: " + message);
            logger.log(message, LogType.MESSAGE);
        }
        return true;
    }
    public void printMsgInConsoleAndSendToAll(String msg) {
        System.out.println(msg);
        for (MessageProcessor client : clientsConnected) {
            if (!client.getUserName().equals(userName)) {
                client.getOutcome().println(msg);
            }
        }
    }
}