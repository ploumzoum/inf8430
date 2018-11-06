package ca.polymtl.inf8480.tp1.shared;

import java.io.*;

public class FileModel implements Serializable
{
    private static final long serialVersionUID = 345547315;
    public String _fileName;
    public String _lastChecksum;
    public boolean _locked;
    public String _lockUID;

    public FileModel(String fileName)
    {
        _fileName = fileName;
        _lastChecksum = "";
        _locked = false;
    }
}