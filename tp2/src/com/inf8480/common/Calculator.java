package com.inf8480.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    int getCapacity() throws RemoteException;
    boolean acceptTask(int operationsQuantity) throws RemoteException;
    Operation[] executeTask(Operation[] task) throws RemoteException;
}
