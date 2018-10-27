package com.inf8480.server;


import com.inf8480.common.Operations;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends OperationsImpl{

    public Server() {}

    public static void main(String[] args) {
        try {
            // Instantiating the implementation class
            OperationsImpl obj = new OperationsImpl();

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            Operations stub = (Operations) UnicastRemoteObject.exportObject(obj, 0);

            // Binding the remote object (stub) in the registry
            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Operations", stub);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
