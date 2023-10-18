package serverapp;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class ServerTest {
    private Server server;
    private int port;

    @Before
    public void setUp() throws IOException {
        port = 9876; 
        server = new Server(port);
    }

    @After
    public void tearDown() {
        server.closeServer();
    }

    @Test
    public void testStartServer() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Thread clientThread = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", port); // Connect to the server
                socket.close(); // Close the client socket
            } catch (IOException e) {
                fail("Client connection failed: " + e.getMessage());
            } finally {
                latch.countDown(); // Signal the test thread that the client is done
            }
        });

        Thread serverThread = new Thread(() -> {
            server.startServer(); // Start the server
        });

        serverThread.start();
        clientThread.start();

        assertTrue(latch.await(5, TimeUnit.SECONDS));

        assertTrue(serverThread.isAlive());
        assertFalse(serverThread.isInterrupted());
    }

    @Test
    public void testCloseServer() {
        // Test the closeServer method

        // Assert that the server is initially open
        assertFalse(server.serverSocket.isClosed());

        // Close the server
        server.closeServer();

        // Assert that the server is closed
        assertTrue(server.serverSocket.isClosed());
    }
}