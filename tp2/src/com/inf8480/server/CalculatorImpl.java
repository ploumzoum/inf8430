package com.inf8480.server;

import com.inf8480.common.Calculator;
import com.inf8480.common.Operation;

import java.util.Random;

public class CalculatorImpl implements Calculator {

    private int _maliciousness;
    private boolean _maliciousMode = false;
    private int _capacity;
    private Random rand;

    private int temperResult(int result) {
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
        if (maliciousness > 0) {
            _maliciousMode = true;
        }
        _capacity = capacity;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public boolean acceptTask(int operationsQuantity) {
        return false;
    }

    @Override
    public Operation[] executeTask(Operation[] task) {
        for (Operation operation: task) {
            switch (operation._name) {
                case "prime": operation._result = OperationsImpl.prime(operation._argument[0]);
                break;
                case "pell": operation._result = OperationsImpl.prime(operation._argument[0]);
                break;
                case "sum" : operation._result = OperationsImpl.sum(operation._argument);
            }

        }
        return task;
    }
}