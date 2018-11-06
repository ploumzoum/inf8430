package tp2;

import tp2.common.Calculator;
import tp2.common.Operation;
import tp2.common.Task;

import java.util.Random;

public class CalculatorImpl implements Calculator {

    private int _maliciousness;
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
        _capacity = capacity;
    }

    @Override
    public int getCapacity() {

        return _capacity;
    }

    @Override
    public boolean acceptTask(int operationsQuantity) {
        int rejection = Math.round((operationsQuantity-(float)_capacity)/(4*(float)_capacity)*100);
        System.out.println("rejection (%): " + rejection + " with ni = " + operationsQuantity);
        int randomNum = rand.nextInt(100 + 1);

        return (randomNum > rejection);
    }

    @Override
    public Task executeTask(Task task) {
        for (Operation operation: task.getOperations()) {
            int result = 0;
            switch (operation._name) {
                case "prime": result = OperationsImpl.prime(operation._argument[0]);
                break;
                case "pell": result = OperationsImpl.pell(operation._argument[0]);
            }

            operation._result = (_maliciousness > 0) ? temperResult(result): result;

        }
        return task;
    }


}