package com.inf8480.distributor;

import com.inf8480.common.Operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Distributor {
    private final String filePath = "src/com/inf8480/distributor/operations/";
    public static void main(String[] args) {
        Distributor distributor = new Distributor();
        distributor.run();
    }

    public Distributor() {
        super();

        operationsStub = loadOperationsStub("127.0.0.1");
    }

    private void run() {
        while(true) {
            if(listenToInput()){
                System.out.println("Calculus in progress...");

            } else {
                break;
            }
        }
        System.out.println("Byebye");
    }

    private Operations operationsStub = null;

    private Operations loadOperationsStub(String hostname) {
        Operations stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 5001);
            stub = (Operations) registry.lookup("Operations");
        } catch (NotBoundException e) {
            System.out.println("Error: Name '" + e.getMessage()
                    + "' not defined in RMIregistry.");
        } catch (AccessException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("RemoteException Error: " + e.getMessage());
        }
        return stub;
    }
    private boolean listenToInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a filename to continue or 'quit' to exit");
        String input = reader.nextLine();

        if (input.equals("quit")) {
            return false;
        } else if (input.isEmpty()){
            System.out.println("Error: No argument entered");
        } else {
            parseFile(input);
        }
        return true;
    }

    private void parseFile(String fileName) {
        try {
            File file = new File(filePath + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
