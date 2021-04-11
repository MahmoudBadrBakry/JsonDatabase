package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Database database;
    private static final String address = "127.0.0.1";
    private static final Integer port = 3000;
    private static boolean waitingRequests;

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Server started!");

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {

            waitingRequests = true;
            database = new Database();
            ExecutorService executor = Executors.newFixedThreadPool(4);

            while (!executor.isShutdown()) {
                Socket socket = server.accept();
                try {

                    executor.submit(() -> handleRequest(socket, executor, server));

                } catch (Exception e) {

                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket socket,ExecutorService executor, ServerSocket server) {
        Gson gson = new Gson();
        ICommand command;
        boolean handleAnotherRequest = true;
        String in;
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            try {
                in = dis.readUTF();
                
                //in += "   ---- threadNumber" + Thread.currentThread().getName()
                System.out.println("Received: " + in);

                command = getCommandFromJson(in);
                
                handleAnotherRequest = command.execute(database);
                
                send(dos, gson.toJson(command.getResponse()));

            } catch (Exception e) {
                send(dos, gson.toJson(new ErrorResponse(e.getMessage())));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
           
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (!handleAnotherRequest) {
            executor.shutdown();
            try {
                server.close();
            } catch (IOException e) {}
        }
    }

    private static ICommand getCommandFromJson(String in) {
        JsonObject inputJson = new Gson().fromJson(in, JsonObject.class);
        CommandFactory commandFactory = new CommandFactory(inputJson);
        return commandFactory.getCommand();
    }

    private static void send(DataOutputStream dos, String string) {
        try {
            dos.writeUTF(string);
            System.out.println("Sent: " + string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
