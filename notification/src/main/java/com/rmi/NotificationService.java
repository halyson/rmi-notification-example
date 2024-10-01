package com.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationService extends Remote {
    void registerObserver(NotificationObserver observer) throws RemoteException;
    void unregisterObserver(NotificationObserver observer) throws RemoteException;
    String pingPong(String message) throws RemoteException;
}
