package ru.geekbrains.lesson_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;
    private boolean endConnection;

    public Server (){
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server is up, waiting for connection...");
            socket = serverSocket.accept();
            System.out.println("Client connected:" + socket);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            endConnection = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while (true){
                            String msgFromClient = in.readUTF();
                            if (msgFromClient.equals("-exit")){
                                endConnection = true;
                                out.writeUTF("-exit");
                                System.out.println("Press enter to exit...");
                                break;
                            }
                            System.out.println("Client message: " + msgFromClient);
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
                    System.out.println("Connection is down!");
                    break;
                }
                System.out.println("Type message to client...");
                out.writeUTF(scanner.nextLine());

            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
