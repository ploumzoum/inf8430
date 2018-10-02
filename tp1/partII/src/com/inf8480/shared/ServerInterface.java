package com.inf8480.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;

public interface ServerInterface extends Remote {
	int execute(int a, int b) throws RemoteException;
	boolean create(String name) throws RemoteException, IOException;
	void register(String username, String password) throws RemoteException;
}