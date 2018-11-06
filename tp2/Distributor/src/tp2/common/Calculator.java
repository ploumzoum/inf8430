package tp2.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    int getCapacity() throws RemoteException;
    boolean acceptTask(int operationsQuantity) throws RemoteException;
    Task executeTask(Task task) throws RemoteException;
}
