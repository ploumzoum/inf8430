package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;

public interface AuthenticationServerInterface extends Remote {
	boolean register(String login, String password) throws RemoteException;
	boolean verify(String login, String password) throws RemoteException;
}