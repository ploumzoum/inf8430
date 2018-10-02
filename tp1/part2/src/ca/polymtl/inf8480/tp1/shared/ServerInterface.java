package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;
import java.util.List;

public interface ServerInterface extends Remote {
	int execute(int a, int b) throws RemoteException;
	boolean create(String name) throws RemoteException, IOException;
	void register(String username, String password) throws RemoteException;
	List<FileModel> list() throws RemoteException;
	byte[] get(String filename, String checksum) throws RemoteException;
	void push(String filename, byte[] content) throws RemoteException;
	LockAnswer lock(String filename, String checksum) throws RemoteException;

}