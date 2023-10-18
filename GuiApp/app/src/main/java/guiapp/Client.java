package guiapp;

import java.time.*;
import java.time.format.*;
import java.net.*;
import java.io.*;

public class Client {
    
    private Socket client;
    private DataOutputStream writer;
    private DataInputStream reader;

    public Client(int PORT) throws IOException {
        this.client = new Socket((String)null, PORT);
        this.writer = new DataOutputStream(client.getOutputStream());
        this.reader = new DataInputStream(new BufferedInputStream(client.getInputStream()));
    }

    public void send(String message) throws IOException {
        writer.writeUTF(message);
        
    }

    public void send(String message, String username, String receiver) throws IOException {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        writer.writeUTF("$$" + username + "$$" + receiver + "$$"+ message + "$$" + formattedTime + "$$");
        
    }

    public String receive(int PORT) throws IOException {
        String output = "";
        boolean retry = true;
        while (retry) {
            System.out.println("Client try");
            try {
                output = reader.readUTF();
                retry = false;
            } catch (Exception e) {
                System.out.println("receive error");
                // Close the existing connection before attempting reconnection
                close();
    
                // Attempt to reconnect
                boolean reconnected = false;
                while (!reconnected) {
                    try {
                        client = new Socket((String) null, PORT);  // Reestablish the connection
                        writer = new DataOutputStream(client.getOutputStream());
                        reader = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                        reconnected = true;
                    } catch (IOException ex) {
                        // Handle the reconnection failure
                        System.out.println("Reconnection failed");
                        // Optionally, you can add a delay here before retrying the reconnection
                    }
                }
            }
        }
        return output;
    }
    public void requestClients(String message) throws IOException{
        writer.writeUTF(message);
        System.out.println("!!" + message);
    }

    public String getClients() throws IOException{
        
        return reader.readUTF();
    }

    public void close() throws IOException {
        client.close();
    }
} 
    

