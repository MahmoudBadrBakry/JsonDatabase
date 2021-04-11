package client;

import com.beust.jcommander.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class Main {
    private static int port = 3000;
    private static String address = "127.0.0.1";
//    private static  Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            ArgsToCommand commandFactory = new ArgsToCommand();

            JCommander.newBuilder().addObject(commandFactory).build().parse(args);

            System.out.println("Client started!");

            String sent = commandFactory.getCommandJSON();
            System.out.println("Sent: " + sent);
            dos.writeUTF(sent);

            String received = dis.readUTF();
            System.out.println("Received: " + received);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
