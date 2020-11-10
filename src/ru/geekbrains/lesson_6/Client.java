package ru.geekbrains.lesson_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;
    private boolean endConnection;

    public Client() {
        try {
            socket = new Socket("localhost", 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            endConnection = false;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while (true){
                            String msgFromServer = in.readUTF();
                            if (msgFromServer.equals("-exit")){
                                endConnection = true;
                                out.writeUTF("-exit");
                                System.out.println("Press enter to exit...");
                                break;
                            }
                            System.out.println("Server message: " + msgFromServer);
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();

            scanner = new Scanner(System.in);
            while (true){
                if (endConnection){
                    out.writeUTF("-exit");
                    System.out.println("Good bye!!!");
                    break;
                }
                System.out.println("Please type your message...");
                out.writeUTF(scanner.nextLine());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
