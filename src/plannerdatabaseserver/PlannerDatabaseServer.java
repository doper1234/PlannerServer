/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plannerdatabaseserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Chris
 */
public class PlannerDatabaseServer {

    private PlannerSQLReader plannerSQLReader;
    private ArrayList connectedPlayerStream;
    private ArrayList connectedIPs;
    private PrintWriter mainPrintWriter;

    public static void main(String[] args) {
        PlannerDatabaseServer plannerDatabaseServer = new PlannerDatabaseServer();
        plannerDatabaseServer.setupServer();
    }

    public void setupServer() {
        connectedPlayerStream = new ArrayList();
        connectedIPs = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(4000);
            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                connectedIPs.add(clientSocket.getInetAddress().getLocalHost().getHostName() + "/" + clientSocket.getInetAddress());
                System.out.println(connectedIPs);
                connectedPlayerStream.add(writer);
                if (mainPrintWriter == null) {
                    mainPrintWriter = writer;
                }

                Thread t = new Thread(new ClientHandler((clientSocket)));
                t.start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void sendOutPlayerLocations() {

        //Iterator it = connectedPlayerStream.iterator();
        for (int j = 0; j < connectedPlayerStream.size(); j++) {
            try {
                PrintWriter writer = (PrintWriter) connectedPlayerStream.get(j);
                if (j == 0) {
                    //writer.println(player +"," +p2Action);
                } else if (j == 1) {
                    //writer.println(player +"," + p1Action);  
                }
                writer.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {

            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            plannerSQLReader = new PlannerSQLReader();
            String message = "nothing";
            boolean broken = false;
            try {
                while (true) {
                    //System.out.println("running");
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                        String result[] = message.split(",");
                        String username = result[1].toLowerCase();
                        if (username.contains(" ")) {
                            char[] editUsername = username.toCharArray();
                            username = "";
                            for (int i = 0; i < editUsername.length; i++) {
                                if (editUsername[i] == ' ') {
                                    editUsername[i] = '_';
                                }
                                username += editUsername[i];
                            }
                        }
                        PrintWriter writer = (PrintWriter) connectedPlayerStream.get(connectedPlayerStream.size() - 1);
                        if (result[0].equalsIgnoreCase("check if login exists")) {
                        
                            checkIfLoginExists(result, username, writer);
                        } else if (result[0].equalsIgnoreCase("create new account")) {
                            
                            createNewAccount(result, username, writer);
                        } else if (result[0].equalsIgnoreCase("get events")) {
                            
                            getEvents(result, username, writer);
                        } else if (result[0].equalsIgnoreCase("add events")) {
                            
                            addEvents(result, username, writer);
                        }

                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void checkIfLoginExists(String[] result, String username, PrintWriter writer) {
        writer.println(plannerSQLReader.doesUsernameExist(username));
        writer.flush();

    }

    private void createNewAccount(String[] result, String username, PrintWriter writer) {
        
        System.out.println("Creating database: "+username);
        plannerSQLReader.createDatabase(username);
        writer.println("New account created");
        writer.flush();
    }

    private void getEvents(String[] result, String username, PrintWriter writer) {
        writer.println("get events, " + plannerSQLReader.getEvents(username));
        writer.flush();
    }

    private void addEvents(String[] result, String username, PrintWriter writer) {
        List<String> events = new ArrayList<>();
        for (int i = 2; i < result.length; i++) {
            
        }
        
        
        for (int i = 2; i < result.length; i++) {
            System.out.println("Event " +result[i]);
            String[] singleEvent = result[i].split("/");
            List<String> event = new ArrayList<>();
            for (int j = 0; j < singleEvent.length; j++) {
                if(singleEvent[j].contains("[")){
                    singleEvent[j] = singleEvent[j].substring(1);
                }
                if(singleEvent[j].contains("null")){
                    singleEvent[j] = "0";
                }
                System.out.println("    EventInfo " +singleEvent[j]);
                events.add(singleEvent[j]);
                event.add(singleEvent[j]);
            }
            plannerSQLReader.addToDatabase(username, event);
        }
        System.out.println(events);
        //plannerSQLReader.addToDatabase(username, events);
        writer.println("Added" + result);
        writer.flush();
    }

    private void sendConnectedPeople() {
        for (int i = 0; i < connectedPlayerStream.size(); i++) {
            PrintWriter writer = (PrintWriter) connectedPlayerStream.get(i);
            for (int j = 0; j < connectedIPs.size(); j++) {
                writer.println((j + 1) + " " + connectedIPs.get(j));
                System.out.println("sending " + j + " " + connectedIPs.get(j) + "to player " + i);
            }
        }
    }

    private void sendStartGameMessage() {
        for (int i = 0; i < connectedPlayerStream.size(); i++) {
            PrintWriter writer = (PrintWriter) connectedPlayerStream.get(i);
            writer.println("start");
        }
    }

}
