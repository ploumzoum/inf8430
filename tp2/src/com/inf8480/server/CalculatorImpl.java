package com.inf8480.server;

import com.inf8480.common.Calculator;
import com.inf8480.common.Operation;

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
}