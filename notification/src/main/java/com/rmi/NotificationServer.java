package com.rmi;

import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationServer implements NotificationService {
    private List<NotificationObserver> observers = new ArrayList<>();

    public NotificationServer() {}

    public void registerObserver(NotificationObserver observer) throws RemoteException {
        observers.add(observer);
        System.out.println("New observer registered");
    }

    public void unregisterObserver(NotificationObserver observer) throws RemoteException {
        observers.remove(observer);
        System.out.println("Observer unregistered");
    }

    public String pingPong(String message) throws RemoteException {
        if ("ping".equalsIgnoreCase(message)) {
            return "pong";
        }
        return "Invalid message. Send 'ping' to get 'pong'.";
    }

    private void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            try {
                observer.update(message);
            } catch (RemoteException e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }

    public static void main(String args[]) {
        try {
            NotificationServer server = new NotificationServer();
            NotificationService stub = (NotificationService) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("NotificationService", stub);

            System.out.println("Server ready");

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(() -> {
                String notification = "Server notification: " + System.currentTimeMillis();
                server.notifyObservers(notification);
            }, 0, 5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
