package com.inf8480.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;
import java.util.List;

/**
 * Created by viled2 on 18-11-05.
 */
public interface NameServiceInterface extends Remote {
    public List<String> fetchAllAvailable();
    public boolean authenticate(String uid, String password);
    public void register(String hostname);
    public void remove(String hostname);
}
