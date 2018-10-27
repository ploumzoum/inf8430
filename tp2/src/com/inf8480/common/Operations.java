package com.inf8480.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Operations extends Remote {
    int prime(int x) throws RemoteException;
    int pell(int x) throws RemoteException;
}
