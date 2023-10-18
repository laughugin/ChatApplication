    package serverapp;

    import java.io.*;
    import java.net.*;
    import java.util.ArrayList;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
    import org.apache.logging.log4j.LogManager;

    public class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String clientUsername = "";
        private DataInputStream socketReader;
        private DataOutputStream socketWriter;
        static {
            clientHandlers = new ArrayList<>();
        }

        private static final Logger logger = LogManager.getLogger(ClientHandler.class);

        String messagesDb = "D:/git/chatapp/SERVERAPP/app/src/main/java/serverapp/db/serverDb.txt";
        String clientDb = "D:/git/chatapp/SERVERAPP/app/src/main/java/serverapp/db/clientsDb.txt";
    
    

        public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        
        public ClientHandler(Socket socket) {
            try {
                this.clientSocket = socket;
                this.socketReader = new DataInputStream(clientSocket.getInputStream());
                this.socketWriter = new DataOutputStream(clientSocket.getOutputStream());
                Path fileUsers = Paths.get(clientDb);


                this.clientUsername = socketReader.readUTF();
                String idList = "";
                String bufferUsers = Files.readString(fileUsers);
                bufferUsers = bufferUsers + this.clientUsername;
                PrintWriter out = new PrintWriter(clientDb);
                out.println(bufferUsers);
                out.close();
                for (ClientHandler clientHandler : clientHandlers) {
                    idList += clientHandler.clientUsername + ", ";
                }
                socketWriter.writeUTF(idList);
                clientHandlers.add(this);
                System.out.println("New client connected: " + this.clientUsername);
                System.out.println("Number of clients: " + clientHandlers.size());
                logger.info("New client connected: " + this.clientUsername);
                logger.info("Number of clients: " + clientHandlers.size());
            } catch (IOException e) {
                logger.error("Error in client connection", e);;
            }
        }

         public ClientHandler(Socket socket, DataInputStream socketReader) {
            try {
                this.clientSocket = socket;
                this.socketReader = socketReader;
                this.socketWriter = new DataOutputStream(clientSocket.getOutputStream());
                Path fileUsers = Paths.get(clientDb);


                this.clientUsername = socketReader.readUTF();
                String idList = "";
                String bufferUsers = Files.readString(fileUsers);
                bufferUsers = bufferUsers + this.clientUsername;
                PrintWriter out = new PrintWriter(clientDb);
                out.println(bufferUsers);
                out.close();
                for (ClientHandler clientHandler : clientHandlers) {
                    idList += clientHandler.clientUsername + ", ";
                }
                socketWriter.writeUTF(idList);
                clientHandlers.add(this);
                System.out.println("New client connected: " + this.clientUsername);
                System.out.println("Number of clients: " + clientHandlers.size());
                logger.info("New client connected: " + this.clientUsername);
                logger.info("Number of clients: " + clientHandlers.size());
            } catch (IOException e) {
                logger.error("Error in client connection", e);;
            }
        }

        public DataInputStream getSocketReader() {
            return socketReader;
        }

        public DataOutputStream getSocketWriter() {
            return socketWriter;
        }

        public void setSocketReader(DataInputStream socketReader) {
            this.socketReader = socketReader;
        }

        public void setSocketWriter(DataOutputStream socketWriter) {
            this.socketWriter = socketWriter;
        }

        @Override
        public void run() {
            try {
                while (clientSocket.isConnected()) {
                    String line = socketReader.readUTF();
                    if (line.equals("getClients")) {
                        String idList = "";
                        for (ClientHandler clientHandler : clientHandlers) {
                            idList += clientHandler.clientUsername + ", ";
                        }
                        System.out.println(idList);
                        socketWriter.writeUTF(idList);
                    }
                    else {
                        
                        String[] partsLine = line.split("\\$\\$");
                        String messageToSend = "";
                        if (partsLine[3]=="" && !partsLine[2].equals("empty")){
                            Path fileMes = Paths.get(messagesDb);
                            if(!Files.exists(fileMes)){
                                Files.createFile(fileMes);
                            }
                            String buffer = Files.readString(fileMes);
                            String Bigparts[] = buffer.split("\n");
                            if(Bigparts.length > 1){
                                for (int i = 0; i < Bigparts.length; i++){
                                    String[] parts = Bigparts[i].split("\\$\\$");

                                    if((parts[1].equals(partsLine[2]) && parts[2].equals(partsLine[1])) || (parts[1].equals(partsLine[1]) && parts[2].equals(partsLine[2]))){
                                        messageToSend += Bigparts[i] + '\n';
                                    }
                                }
                                socketWriter.writeUTF(messageToSend);
                            }
                            else{
                                socketWriter.writeUTF("");
                            }
                        }
                        else if(!partsLine[2].equals("empty")){
                            Path fileMes = Path.of(messagesDb);
                            String buffer = Files.readString(fileMes);
                            buffer = buffer + line;
                            PrintWriter out = new PrintWriter(messagesDb);
                            out.println(buffer);
                            out.close();
                            

                            String Bigparts[] = buffer.split("\n");
                            for (int i = 0; i < Bigparts.length; i++){
                                String[] parts = Bigparts[i].split("\\$\\$");

                                if((parts[1].equals(partsLine[2]) && parts[2].equals(partsLine[1])) || (parts[1].equals(partsLine[1]) && parts[2].equals(partsLine[2]))){
                                    messageToSend += Bigparts[i] + '\n';
                                }
                            }
                            socketWriter.writeUTF(messageToSend);
                        }
                    }
                    try{
                        Thread.sleep(100);
                    } catch(Exception e){
                        
                    }
                }
            } catch (IOException e) {
                logger.error("Error handling client connection", e);
                close();
            }
        }

        public void removeClientHandler() {
            clientHandlers.remove(this);
        }

        public void close() {
            removeClientHandler();
            removeClientHandler();
            try {
                if (socketReader != null) {
                    socketReader.close();
                }
                if (socketWriter != null) {
                    socketWriter.close();
                }
            } catch (IOException e) {
                logger.error("Error closing input/output streams", e);
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        logger.error("Error closing client socket", e);
                    }
                }
            }
        }
    }