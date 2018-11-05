package com.inf8480.common;

import java.io.Serializable;

public class Operation implements Serializable {
    public String _name;
    public int[] _argument;
    public int _result = -1;

    public Operation(String name, int[] argument) {
        _name = name;
        _argument = argument;
    }

}
