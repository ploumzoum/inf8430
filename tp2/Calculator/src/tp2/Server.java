package tp2;

import tp2.common.Calculator;
import tp2.common.NameServiceInterface;

import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {


    public Server() {}

    public static void main(String[] args) {
        Server server = new Server();

        if (args.length == 2) {
            server.run(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            server.run(0, 100);
        }

    }
    private String _hostname = "127.0.0.1";
    private NameServiceInterface loadNameService(String hostname)
    {
        NameServiceInterface stub = null;
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 5010);
            stub = (NameServiceInterface) registry.lookup("nameService");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage()
                    + "' n'est pas défini dans le registre RMI.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }

    private void run(int maliciousness, int capacity) {
        try {

            // Instantiating the implementation class
            CalculatorImpl obj = new CalculatorImpl(maliciousness, capacity);

            // Exporting the object of implementation class
            // (here we are exporting the remote object to the stub)
            Calculator skeleton = (Calculator) UnicastRemoteObject.exportObject(obj, 5001);

            // Binding the remote object (stub) in the registry
            // Registry registry = LocateRegistry.getRegistry()

            Registry registry = LocateRegistry.createRegistry(5001);

            registry.rebind("Calculator", skeleton);
            NameServiceInterface nameServiceStub = loadNameService(_hostname);
            String hostname = InetAddress.getLocalHost().getHostAddress();
            nameServiceStub.register(hostname);
            System.out.println("tp2.Server ready.\n* Maliciousness: " + maliciousness + "\n* Capacity: " + capacity);


        } catch (Exception e) {
            System.err.println("tp2.Server exception: " + e.toString());
            e.printStackTrace();
        }
    }





}
