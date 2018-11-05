package com.inf8480.server;

import com.inf8480.common.Calculator;
import com.inf8480.common.NameServiceInterface;
import com.inf8480.common.Operation;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class CalculatorImpl implements Calculator {

    private int _maliciousness;
    private int _capacity;
    private Random rand;

    private int processResult(int result) {
        int randomNum = rand.nextInt(100 + 1);
        if(randomNum < _maliciousness) {
            result += randomNum;
        }
        return result;
    }

    CalculatorImpl(int maliciousness, int capacity) {
        super();
        rand = new Random();
        _maliciousness = maliciousness;
        _capacity = capacity;
        nameServiceStub = loadNameService("127.0.0.1");
        try {
            nameServiceStub.register("127.0.0.1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getCapacity() {

        return _capacity;
    }

    @Override
    public boolean acceptTask(int operationsQuantity) {
        int rejection = (operationsQuantity - _capacity)/(4*_capacity)*100;
        int randomNum = rand.nextInt(100 + 1);

        return randomNum < rejection;
    }

    @Override
    public ArrayList<Operation> executeTask(ArrayList<Operation> task) {
        for (Operation operation: task) {
            int result = 0;
            switch (operation._name) {
                case "prime": result = OperationsImpl.prime(operation._argument[0]);
                break;
                case "pell": result = OperationsImpl.pell(operation._argument[0]);
                break;
                case "sum" : result = OperationsImpl.sum(operation._argument);
            }

            operation._result = result;

        }
        return task;
    }

    private NameServiceInterface nameServiceStub;
    private NameServiceInterface loadNameService(String hostname)
    {
        NameServiceInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 5010);
            stub = (NameServiceInterface) registry.lookup("nameService");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage()
                    + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }
}