package com.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class NotificationClient implements NotificationObserver {
    private NotificationService service;

    public NotificationClient() {}

    public void startClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (NotificationService) registry.lookup("NotificationService");

            NotificationObserver stub = (NotificationObserver) UnicastRemoteObject.exportObject(this, 0);
            service.registerObserver(stub);

            System.out.println("Client ready to receive notifications");


            // Add ping-pong functionality
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter 'ping' or 'exit': ");
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }
                String response = service.pingPong(input);
                System.out.println("Server response: " + response);
            }
            scanner.close();
            service.unregisterObserver(stub);



        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void update(String message) {
        System.out.println("Received notification: " + message);
    }

    public static void main(String[] args) {
        NotificationClient client = new NotificationClient();
        client.startClient();
    }
}

