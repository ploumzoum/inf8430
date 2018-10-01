package ca.polymtl.inf8480.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;
import java.util.Scanner;
import java.util.List;

import ca.polymtl.inf8480.tp1.shared.*;

public class Client {
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

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

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

			if (localServerStub != null) {
				appelRMILocal();
			}

			if (distantServerStub != null) {
				appelRMIDistant();
			}
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
		long start = System.nanoTime();
		int result = localServer.execute(4, 7);
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
						result2 = localServer.create(argument1);
					}
					catch (IOException e)
					{
						System.out.println("Erreur: " + e.getMessage());
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
			default: 
		
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel normal: " + (end - start)
					+ " ns");
			System.out.println("Résultat appel normal: " + result);
		}
	}

	private void appelRMILocal() {
		try {
			long start = System.nanoTime();
			int result = localServerStub.execute(4, 7);
			long end = System.nanoTime();
			System.out.println("Temps écoulé appel RMI local: " + (end - start)
					+ " ns");
			System.out.println("Résultat appel RMI local: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void appelRMIDistant() {
		try {
			long start = System.nanoTime();
			int result = distantServerStub.execute(4, 7);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI distant: "
					+ (end - start) + " ns");
			System.out.println("Résultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
