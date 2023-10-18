package serverapp;
import java.io.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Main {
    private static final int PORT = 9876;
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Starting the server");
        Server server = new Server(PORT);
        server.startServer();
        logger.info("Server started on port " + PORT);
    }
}