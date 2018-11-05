package com.inf8480.distributor;

import com.inf8480.common.Calculator;
import com.inf8480.common.Operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Distributor {
    private final String filePath = "src/com/inf8480/distributor/inputFiles/";
    private String userInput  = "";
    private ArrayList<Operation> operations = new ArrayList<>();
    public static void main(String[] args) {
        Distributor distributor = new Distributor();
        distributor.run();
    }

    public Distributor() {
        super();

        stub = loadCalculatorStub("127.0.0.1");
    }

    private void run() {
        int result;
        while(listenToInput()) {
            parseFile();
            result = dispatchOperations();
            System.out.println("Result: " + result);
            operations.clear();
        }
        System.out.println("Byebye");
    }

    private Calculator stub;

    private Calculator loadCalculatorStub(String hostname) {
        Calculator stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 5001);
            stub = (Calculator) registry.lookup("Calculator");
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
        userInput = reader.nextLine();

        if (userInput.equals("quit")) {
            return false;
        } else if (userInput.isEmpty()){
            System.out.println("Error: No argument entered");
        }
        return true;
    }

    private void parseFile() {
        try {
            File file = new File(filePath + userInput);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parsed = line.split("\\s+");
                try {
                    int[] argument = {Integer.parseInt(parsed[1])};
                    operations.add(new Operation(parsed[0], argument));
                } catch (Exception e) {
                    System.out.println("Error:" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private int dispatchOperations() {
        // implement server selection logic here
        while(!isTaskCompleted()) {
            try {
                operations = stub.executeTask(operations);
            } catch (RemoteException e) {
                System.err.println("Remote Exception" + e.getMessage());
            }
        }

        Operation sumItAll = new Operation("sum", extractResults());
        operations.add(sumItAll);
        try {
            operations = stub.executeTask(operations);
        } catch (RemoteException e) {
            System.err.println("Remote Exception" + e.getMessage());
        }
        return operations.get(0)._result;
    }

    private boolean isTaskCompleted() {
        for (Operation operation: operations) {
            if (operation._result == -1) {
                return false;
            }
        }
        return true;
    }
    private int[] extractResults()
    {
        int[] array = new int[operations.size()];
        Iterator<Operation> iterator = operations.iterator();
        for (int i = 0; i < array.length; i++)
        {
            array[i] = iterator.next()._result;
        }
        operations.clear();
        return array;
    }
}
