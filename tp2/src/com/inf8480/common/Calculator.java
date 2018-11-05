package com.inf8480.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Calculator extends Remote {
    int getCapacity() throws RemoteException;
    boolean acceptTask(int operationsQuantity) throws RemoteException;
    ArrayList<Operation> executeTask(ArrayList<Operation> task) throws RemoteException;
}
