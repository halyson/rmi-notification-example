package com.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationObserver extends Remote {
    void update(String message) throws RemoteException;
}
