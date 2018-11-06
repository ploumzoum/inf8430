package tp2.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {
    private ArrayList<Operation> _operations;

    public ArrayList<Operation> getOperations() {
        return _operations;
    }

    public Task(ArrayList<Operation> operations) {
        _operations = operations;
    }
    public void add(Operation operation) {
        _operations.add(operation);
    }

    public void append(Task task) {
        _operations.addAll(task._operations);
    }

    public int size() {
        return _operations.size();
    }

    public ArrayList<Operation> shrink(int fromIndex, int toIndex){
        ArrayList<Operation> chopped2 = new ArrayList<>(_operations.subList(fromIndex, toIndex));
        _operations = new ArrayList<>(_operations.subList(0, fromIndex));
        return chopped2;
    }
}
