package tp2.common;

import java.io.Serializable;
import java.util.UUID;

public class Operation implements Serializable {
    public String _name;
    public int[] _argument;
    public int _result = -1;
    public UUID _ID;


    public Operation(String name, int[] argument) {
        _name = name;
        _argument = argument;
        _ID = UUID.randomUUID();

    }

}
