package tp2.common;

/**
 * Created by viled2 on 18-11-05.
 */
public class CalculatorModel {

    public CalculatorModel(Calculator stub, int capacity)
    {
        serverStub = stub;
        serverCapacity = capacity;

    }
    public Calculator serverStub;
    public int serverCapacity;
}
