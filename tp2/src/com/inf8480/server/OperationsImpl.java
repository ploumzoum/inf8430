package com.inf8480.server;

import com.inf8480.common.Operations;

import java.util.Random;


public class OperationsImpl implements Operations {


    private int _maliciousness;
    private boolean _maliciousMode = false;
    private Random rand;

    private int temperResult(int result) {
        int randomNum = rand.nextInt(100 + 1);
        boolean decision = randomNum < _maliciousness;
        System.out.println("randomNum " + randomNum);
        System.out.println("temperResult ? " + decision);
        if(decision) {
            result += randomNum;
        }
        return result;
    }

     OperationsImpl(int maliciousness) {
        super();
        rand = new Random();
        _maliciousness = maliciousness;
        if (_maliciousness > 0) {
            _maliciousMode = true;
        }
    }
    @Override
    public int sum(int[] results) {
        int sum = 0;
        for (int result: results) {
            sum = (sum + result) % 4000;
        }
        return (_maliciousMode) ? temperResult(sum) : sum;
    }
    /**
     * Methodes utilitaires pour effectuer les operations du TP2.
     *
     * L'implementation des operations est volontairement non-optimale.
     *
     * @author Simon Delisle et Francois Doray
     *
     */
    @Override
    public int pell(int x) {
        int result = computePell(x);
        return (_maliciousMode) ? temperResult(result) : result;
    }

    private int computePell(int x) {
        if (x == 0)
            return 0;
        if (x == 1)
            return 1;
        return 2 * computePell(x - 1) + computePell(x - 2);
    }



    @Override
    public int prime(int x) {
        int highestPrime = 0;

        for (int i = 1; i <= x; ++i)
        {
            if (isPrime(i) && x % i == 0 && i > highestPrime)
                highestPrime = i;
        }

        return (_maliciousMode) ? temperResult(highestPrime) : highestPrime;
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
