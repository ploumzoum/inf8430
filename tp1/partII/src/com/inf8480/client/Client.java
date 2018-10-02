package com.inf8480.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;
import java.util.List;
import java.util.Scanner;

import com.inf8480.shared.FileModel;
import com.inf8480.shared.ServerInterface;

public class Client {
	private final String clientPath = "src/com/inf8480/client";
	public static void main(String[] args) {
		String distantHostname = null;

		if (args.length > 0) {
			distantHostname = args[0];
		}

		Client client = new Client(distantHostname);
		client.run();
	}

	FakeServer localServer = null; // Pour tester la latence d'un appel de
									// fonction normal.
	private ServerInterface localServerStub = null;
	private ServerInterface distantServerStub = null;

	public Client(String distantServerHostname) {
		super();

//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new SecurityManager());
//		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	private void run() {
		while(true)
		{
			appelNormal();
		}
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage()
					+ "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	private void appelNormal() {
		Scanner reader = new Scanner(System.in);
		System.out.print("client$ ");
		String input = reader.nextLine();

		String[] parsed = input.split("\\s");
		String cmd = parsed[0];
		String argument1 = "";
		if(parsed.length == 2)
		{
			argument1 = parsed[1];
		}
		boolean result2 = false;

		switch (cmd)
		{
			case "create":
				if(argument1 != null)
				{
					try
					{
						result2 = localServerStub.create(argument1);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					System.out.println("Résultat appel normal create: " + result2);
				}
				else
				{
					System.out.println("Argument non-fourni.");
				}
			break; 
			
			case "list":
				List<FileModel> list  = localServer.list();
				for (FileModel file : list) {
					System.out.println(file._fileName);
				}
			break;

			case "syncLocalDirectory":
				List<FileModel> serverList = localServer.list();
				for (FileModel file : serverList) {
					File newFile = new File( clientPath + "/FileSystemClient/" + file._fileName + ".txt");
					try
					{
						newFile.createNewFile();
					}
					catch (IOException e)
					{
						System.err.println("Erreur: " + e.getMessage());
					}
				}
				System.out.println("Synchronization completed.");
			break;
			default: 
				System.err.println("Erreur: commande non reconnue.");

		}
	}




}
