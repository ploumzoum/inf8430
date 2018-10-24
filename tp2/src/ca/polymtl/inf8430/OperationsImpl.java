package ca.polymtl.inf8430;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OperationsImpl extends UnicastRemoteObject
implements Operations{

    protected OperationsImpl() throws RemoteException {
        super();
    }

    public int pell(int x) throws RemoteException {
        if (x == 0)
            return 0;
        if (x == 1)
            return 1;
        return 2 * pell(x - 1) + pell(x - 2);
    }

    public int prime(int x) throws RemoteException {
        int highestPrime = 0;

        for (int i = 1; i <= x; ++i)
        {
            if (isPrime(i) && x % i == 0 && i > highestPrime)
                highestPrime = i;
        }

        return highestPrime;
    }

    private static boolean isPrime(int x) {
        if (x <= 1)
            return false;

        for (int i = 2; i < x; ++i)
        {
            if (x % i == 0)
                return false;
        }
        return true;
    }
}
