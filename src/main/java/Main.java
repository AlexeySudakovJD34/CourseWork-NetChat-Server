import server.Server;

public class Main {
    public static void main(String[] args) {
        Server serverSuChat = new Server();
        serverSuChat.startServer();
        serverSuChat.waitingForConnection();
    }
}
