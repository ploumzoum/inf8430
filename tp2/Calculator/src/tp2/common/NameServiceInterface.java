package tp2.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by viled2 on 18-11-05.
 */
public interface NameServiceInterface extends Remote {
    public List<String> fetchAllAvailable() throws RemoteException;
    public boolean authenticate(String uid, String password) throws RemoteException;
    public void register(String hostname) throws RemoteException;
    public void remove(String hostname) throws RemoteException;
}
