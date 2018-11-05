package com.inf8480.server;


import com.inf8480.common.Calculator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Server {


    public Server() {}

    public static void main(String[] args) {
        Server server = new Server();

        if (args.length == 2) {
            server.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            server.run(0, 100);
        }

    }

    private void run(int maliciousness, int capacity) {
        try {

            // Instantiating the implementation class
            CalculatorImpl obj = new CalculatorImpl(maliciousness, capacity);

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            Calculator skeleton = (Calculator) UnicastRemoteObject.exportObject(obj, 5001);

            // Binding the remote object (stub) in the registry
            // Registry registry = LocateRegistry.getRegistry()

            // To run server at the moment without the distributor
            Registry registry = LocateRegistry.createRegistry(5001);

            registry.rebind("OperationsImpl", skeleton);
            System.out.println("Server ready, maliciousness is set to " + maliciousness);


        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }





}
