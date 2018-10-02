package ca.polymtl.inf8480.tp1.shared;

import java.io.*;

public class LockAnswer
{
    public byte[] _file;
    public boolean _lockSuccess;
    public String _UID;

    public LockAnswer(byte[] file, boolean lockSuccess, String UID)
    {
        _file = file;
        _lockSuccess = lockSuccess;
        _UID = UID;
    }
}