package serverapp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {
    private ClientHandler clientHandler;
    @Mock
    private Socket mockClientSocket;

    @Mock
    private DataInputStream mockSocketReader;
    @Mock
    private DataOutputStream mockSocketWriter;

    @Before
    public void setUp() throws IOException {
        // Create the mock objects
        mockClientSocket = mock(Socket.class);
        mockSocketReader = mock(DataInputStream.class);
        mockSocketWriter = mock(DataOutputStream.class);

        // Set up the behavior of the socket-related method calls
        InputStream mockInputStream = mock(InputStream.class);
        when(mockClientSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockClientSocket.getOutputStream()).thenReturn(mockSocketWriter);

        // Set up the behavior of the socket reader's method calls
        when(mockSocketReader.readUTF()).thenReturn("getClients");
        
        // Set up the behavior of the input stream's method calls
        when(mockSocketReader.read()).thenReturn(1); // Example stubbing, adjust according to your needs
        
        clientHandler = new ClientHandler(mockClientSocket, mockSocketReader);
        // Create the ClientHandler instance with the mocked objects
    }

    @After
    public void tearDown() {
        clientHandler.close();
    }

    @Test
    public void testRun() {
        try {
            when(mockSocketReader.readUTF()).thenReturn("getClients");

            clientHandler.run();

            verify(mockSocketReader).readUTF();
            verify(mockSocketWriter).writeUTF("some unexpected string");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRemoveClientHandler() {
        clientHandler.removeClientHandler();

        assertFalse(ClientHandler.clientHandlers.contains(clientHandler));
    }

    @Test
    public void testClose() throws IOException {
        clientHandler.close();

        verify(mockSocketReader).close();
        verify(mockSocketWriter).close();
        verify(mockClientSocket).close();
    }
}