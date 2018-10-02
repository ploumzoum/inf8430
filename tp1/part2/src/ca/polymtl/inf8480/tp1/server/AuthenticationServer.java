package ca.polymtl.inf8480.tp1.server;

import ca.polymtl.inf8480.tp1.shared.UserModel;
import ca.polymtl.inf8480.tp1.shared.AuthenticationServerInterface;


import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationServer implements AuthenticationServerInterface {
    private final String serverPath = "src/ca/polymlt/inf8480/tp1/server";
    private List<UserModel> userList;

	public static void main(String[] args) {

		AuthenticationServer authenticationServer = new AuthenticationServer();
		authenticationServer.run();
    }
    
    private void run() {
        //		if (System.getSecurityManager() == null) {
        //			System.setSecurityManager(new SecurityManager());
        //		}
        
        try {
            AuthenticationServerInterface stub = (AuthenticationServerInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("authenticationServer", stub);
            System.out.println("Authentication Server ready.");
        } catch (ConnectException e) {
            System.err
                    .println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    @Override
	public boolean register(String login, String password ) {

        if (isAvailable(login)) {
            UserModel userModel = new UserModel(login, password);
		    userList = LoadUsersFromSave();
		    userList.add(userModel);
            SaveUserList(userList);
        } else {
            return false;
        }
        return true;
		
    }

    private boolean isAvailable(String login) {
        for (UserModel user : userList
             ) {
            if (user._userName.equalsIgnoreCase(login)) {
                return false;
            }
        }

        return true;
    }
    
    List<UserModel> LoadUsersFromSave()
	{
		List<UserModel> userList = new ArrayList<UserModel>();
		userList = getUserModels(userList, serverPath);
		return userList;
    }
    public static List<UserModel> getUserModels(List<UserModel> userList, String serverPath) {
		try
		{
			FileInputStream fileIn = new FileInputStream(serverPath + "/Save/userList.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try
			{
				userList = (List<UserModel>) in.readObject();
			}
			catch (ClassNotFoundException e)
			{
                System.err.println("Erreur: " + e.getMessage());
			}
			in.close();
			fileIn.close();
		}
		catch (IOException e)
		{
            System.err.println("Erreur: " + e.getMessage());
		}
		return userList;
    }
    
    void SaveUserList(List<UserModel> list)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(serverPath + "/Save/userList.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(list);
			out.close();
			fileOut.close();
		}
		catch(IOException e)
		{
			System.err.println("Erreur: " + e.getMessage());
		}
    }
    
    @Override
    public boolean verify(String login, String password) {
        for (UserModel user : userList
             ) {
            if (user._userName.equalsIgnoreCase(login) && user._password.equalsIgnoreCase(password)) {
                return true;
            }
        }

        return false;
    }
}