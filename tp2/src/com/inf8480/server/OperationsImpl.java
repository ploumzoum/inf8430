package com.inf8480.server;

import com.inf8480.common.Operations;

import java.rmi.RemoteException;

public class OperationsImpl implements Operations {


    /**
     * Methodes utilitaires pour effectuer les operations du TP2.
     *
     * L'implementation des operations est volontairement non-optimale.
     *
     * @author Simon Delisle et Francois Doray
     *
     */
    @Override
    public int pell(int x) throws RemoteException {
        if (x == 0)
            return 0;
        if (x == 1)
            return 1;
        return 2 * pell(x - 1) + pell(x - 2);
    }

    @Override
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
