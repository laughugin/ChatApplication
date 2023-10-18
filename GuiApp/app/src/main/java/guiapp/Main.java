package guiapp;
import java.io.IOException;
import java.lang.Thread;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import guiapp.View;
import guiapp.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final int PORT = 9876;
    private static final Logger logger = LogManager.getLogger(Main.class);
    
    public static String[] removeElementByIndex(String[] arr, int index) {
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }
        String[] newArray = new String[arr.length - 1];
        System.arraycopy(arr, 0, newArray, 0, index);
        System.arraycopy(arr, index + 1, newArray, index, arr.length - index - 1);
        return newArray;
    }

    public static void main(String[] args) throws IOException {
        View frame = new View();
        logger.info("GUI started");
        frame.LogFrame();

        Client client = null;

        boolean isConnected = false;
        while (!isConnected) {
            try {
                client = new Client(PORT);
                logger.info("new Client has been added");
                isConnected = true;
            } catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException exception1) {
                    // handle the exception
                }
            }
        }

        while (true) {
            if (!frame.username.equals("empty")) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // handle the exception
            }
        }

        boolean retry = true;
        while (isConnected) {
            while (retry) {
                try {
                    client.send(frame.username);
                    logger.info("Sending username to the server");
                    retry = false;
                } catch (Exception e) {
                    // Reconnection
                    try {
                        client.close();
                        client = new Client(PORT);
                    } catch (IOException ex) {
                        // Handle the exception
                    }
                }
            }
            retry = true;
            String idList = "";

            while (retry) {
                try {
                    idList = client.receive(PORT);
                    retry = false;
                } catch (Exception e) {
                    System.out.println("Retrying...");
                    // Reconnection
                    try {
                        client.close();
                        client = new Client(PORT);
                    } catch (IOException ex) {
                        // Handle the exception
                    }
                }
            }

            String clientIds[] = idList.split(",\\s*");
            for (int i = 0; i < clientIds.length; i++) {
                clientIds[i] = clientIds[i].replaceAll("\\[|\\]", "");
            }
            frame.clientIds = clientIds;
            frame.Frame();

            frame.userList.setListData(frame.clientIds);

            while (isConnected) {
                for (int i = 0; i < 10e6; i++) {
                }

                if (!frame.receiverString.isEmpty()) {
                    retry = true;
                    while (retry) {
                        try {
                            client.send(frame.tempMessage, frame.username, frame.receiverString);
                            logger.info("Sending message to the server");
                            retry = false;
                        } catch (Exception e) {
                            // reconnection
                            try {
                                client.close();
                                client = new Client(PORT);
                            } catch (IOException ex) {
                                // handle the exception
                            }
                        }
                    }
                    frame.tempMessage = "";
                    retry = true;
                    while (retry) {
                        try {
                            frame.ServerMessage = client.receive(PORT);
                            logger.info("Receiving message from the server");
                            retry = false;
                        } catch (Exception e) {
                            // reconnection 
                            try {
                                client.close();
                                client = new Client(PORT);
                            } catch (IOException ex) {
                                // handle the exception
                            }
                        }
                    }
                    frame.printMessage();
                }

                if (frame.refreshExecuted) {
                    logger.info("Refresh button executed");
                    logger.info("Request server for clients");
                    client.requestClients("getClients");
                    idList = client.getClients();
                    String clientIdsRefreshed[] = idList.split(",\\s*");
                    List<String> list = new ArrayList<String>(Arrays.asList(clientIdsRefreshed));
                    list.remove(frame.username);
                    frame.userList.setListData(list.toArray());
                    while (!frame.clientChoose) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            // handle the exception
                        }
                    }
                    frame.refreshExecuted = false;
                    frame.clientChoose = false;
                } else {
                    try {
                        Thread.sleep(50);
                        continue;
                    } catch (Exception e) {
                        // handle the exception
                    }

                }
            }
        }
    }
}