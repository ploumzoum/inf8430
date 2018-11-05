package com.inf8480.nameservice;


import com.inf8480.common.NameServiceInterface;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
            NameServiceInterface stub = (NameServiceInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("nameService", stub);
            System.out.println("Name Server ready.");
        } catch (ConnectException e) {
            System.err
                    .println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
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
