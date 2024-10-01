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

            // create getenv for RMI_HOST
            String rmiHost = System.getenv("RMI_HOST");
            if (rmiHost == null || rmiHost.isEmpty()) {
                // Provide a default value or throw an exception if the environment variable is not set
                rmiHost = "localhost"; // Default value
                // Alternatively, you could throw an exception:
                // throw new IllegalStateException("RMI_HOST environment variable is not set");
            }
            Registry registry = LocateRegistry.getRegistry(rmiHost, 1099);
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

