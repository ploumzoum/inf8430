package com.inf8480.server;


import com.inf8480.common.Operations;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {


    public Server() {}

    public static void main(String[] args) {
        try {
            int maliciousness;
            if (args.length > 0) {
                maliciousness = Integer.parseInt(args[0]);
            } else {
                maliciousness = 0;
            }
            // Instantiating the implementation class
            OperationsImpl obj = new OperationsImpl(maliciousness);

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            Operations skeleton = (Operations) UnicastRemoteObject.exportObject(obj, 5001);

            // Binding the remote object (stub) in the registry
            // Registry registry = LocateRegistry.getRegistry()

            // To run server at the moment without the distributor
            Registry registry = LocateRegistry.createRegistry(5001);

            registry.rebind("Operations", skeleton);
            System.out.println("Server ready, maliciousness is set to " + maliciousness);


        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
