package com.example.cinema_app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server;

    public Server(){
        System.out.println("initializing the Server socket");
        try {
            server = new ServerSocket(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void serviceLoop(){
        System.out.println("Server: Start service loop.");
        try{
            while(true){
                Socket client = server.accept();
                ClientHandler handler = new ClientHandler(client);
                new Thread(handler).start();
            }
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Server: Finished service loop.");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.serviceLoop();
        System.out.println("Server: Finished");
        System.exit(0);
    }
}
