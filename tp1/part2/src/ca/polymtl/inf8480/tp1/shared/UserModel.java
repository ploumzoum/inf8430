package ca.polymtl.inf8480.tp1.shared;

import java.io.*;

public class UserModel implements Serializable
{
    private static final long serialVersionUID = 345547316;
    public String _userName;
    public String _password;

    public UserModel(String username, String password)
    {
        _userName = username;
        _password = password;
    }
}