<<<<<<< Updated upstream:tp1/ResponseTime_Analyzer/src/ca/polymtl/inf8480/tp1/shared/ServerInterface.java
package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	int execute(int a, int b) throws RemoteException;
}
=======
package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	int execute(byte[] data, int size) throws RemoteException;
}
>>>>>>> Stashed changes:tp1/part1/src/ca/polymtl/inf8480/tp1/shared/ServerInterface.java
