package com.inf8480.nameservice;


import com.inf8480.common.NameServiceInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Annuaire  implements NameServiceInterface{


    public Annuaire() {
        _registeredHosts = new ArrayList<String>();
    }
    public static ArrayList<String> _registeredHosts;
    public static void main(String[] args) {
        Annuaire server = new Annuaire();
        server.run();
    }

    private void run() {
        try {
            // Binding the remote object (stub) in the registry
            // Registry registry = LocateRegistry.getRegistry()

            // To run server at the moment without the distributor
            Registry registry = LocateRegistry.createRegistry(5010);

            registry.rebind("OperationsImpl", skeleton);
            System.out.println("Server ready, maliciousness is set to " + maliciousness);


        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public List<String> fetchAllAvailable()
    {
        List<String> availableHosts = new ArrayList<String>();
        for (String host: _registeredHosts) {
            availableHosts.add(host);
        }
        return availableHosts;
    }

    public boolean authenticate(String uid, String password)
    {
        //TODO: Actually make a db and check
        return true;
    }

    public void register(String hostname)
    {
        if(!_registeredHosts.contains(hostname)) {
            _registeredHosts.add(hostname);
        }
    }

    public void remove(String hostname)
    {
        _registeredHosts.remove(hostname);
    }





}
