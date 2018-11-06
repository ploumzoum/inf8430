package tp2;

import tp2.common.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Distributor {
    private String _userInput = "";
    private Map<UUID, Operation> _operations = new HashMap<>();
    private ArrayList<Task> _taskList = new ArrayList<>();
    private ArrayList<CalculatorModel> calculators = new ArrayList<>();
    private String _hostname = "127.0.0.1";
    public static void main(String[] args) {
        Distributor distributor = new Distributor();
        distributor.run();
    }

    public Distributor() {
        super();
        loadCalculators();
    }

    private void run() {
        int result;
        while(listenToInput()) {
            parseFile();
            result = dispatchTasks();
            System.out.println("Result: " + result);
            _taskList.clear();
        }
        System.out.println("Byebye");
    }


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
            System.err.println("RemoteException Error: " + e.getMessage());
        }
        return stub;
    }
    private boolean listenToInput() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a filename to continue or 'quit' to exit");
        _userInput = reader.nextLine();

        if (_userInput.equals("quit")) {
            return false;
        } else if (_userInput.isEmpty()){
            System.out.println("Error: No argument entered");
        }
        return true;
    }

    private void parseFile() {
        try {
            File file = new File(System.getProperty("user.dir") + "/inputFiles/" + _userInput);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            Task initialTask = new Task(new ArrayList<>());
            while ((line = reader.readLine()) != null) {
                String[] parsed = line.split("\\s+");
                try {
                    int[] argument = {Integer.parseInt(parsed[1])};
                    Operation operation = new Operation(parsed[0], argument);
                    initialTask.add(operation);
                    _operations.put(operation._ID, operation);
                } catch (Exception e) {
                    System.out.println("Error:" + e.getMessage());
                }
            }
            _taskList.add(initialTask);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private int dispatchTasks() {
        // implement server selection logic here

        while(_taskList.size() > 0) {
            Calculator calculatorStub = calculators.get(0).serverStub;
            try {
                int optimalSize = getOptimalSize(calculatorStub);
                Task task = _taskList.get(0);
                if (task.size() > optimalSize) {
                    Task choppedTask = new Task(task.shrink(optimalSize, task.size()));
                    _taskList.add(choppedTask);
                }
                while(!calculatorStub.acceptTask(task.size())) {
                    Task choppedTask = new Task(task.shrink(task.size() - 6, task.size()));
                    _taskList.get(_taskList.size() - 1).append(choppedTask);
                }
                updateMainTask(calculatorStub.executeTask(task));
                _taskList.remove(0);
            } catch (RemoteException e) {
                System.err.println("Remote Exception Error:" + e.getMessage());
                System.out.println("\nReloading calculators...\n");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } finally {
                loadCalculators();
            }
        }
        return sumThemAll(extractResults());
    }

    private void updateMainTask(Task task) {
        for (Operation operation: task.getOperations()) {
            _operations.get(operation._ID)._result = operation._result;
        }
    }
    private int getOptimalSize(Calculator stub) throws RemoteException {
        return Math.round((4 * 20 + 100) * (float) stub.getCapacity() / 100);
    }

    private int[] extractResults(){
        int[] array = new int[_operations.size()];
        int index = 0;
        for (Map.Entry<UUID, Operation> entry : _operations.entrySet())
        {
            array[index] = entry.getValue()._result;
            index++;
        }
        _operations.clear();
        return array;
    }


    private int sumThemAll(int[] results) {
        int sum = 0;
        for (int result: results) {
            sum = (sum + result) % 4000;
        }
        return sum;
    }

    private void loadCalculators()
    {
        try {
            Registry registry = LocateRegistry.getRegistry(_hostname, 5010);
            NameServiceInterface nameServiceStub = (NameServiceInterface) registry.lookup("nameService");
            List<String> availableHosts = nameServiceStub.fetchAllAvailable();
            for (String host : availableHosts) {
                Calculator stub = loadCalculatorStub(host);
                calculators.add(new CalculatorModel(stub, stub.getCapacity()));
            }
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage()
                    + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur chargement service de nom: " + e.getMessage());
        }
    }
}
