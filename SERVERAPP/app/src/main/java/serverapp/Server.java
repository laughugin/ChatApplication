package serverapp;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    public ServerSocket serverSocket;
    public static ArrayList<String> clientIds = new ArrayList<>();

    public Server(int PORT) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.serverSocket.setSoTimeout(600000);
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
                logger.info("New client connected");
            }
        } catch(IOException e) {
            logger.error("Error accepting client connection", e);
        }

    }

    public void closeServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
                logger.info("Server closed");
            }
        } catch(IOException e) {
            logger.warn("Server Was Already closed");
        }
    }
}