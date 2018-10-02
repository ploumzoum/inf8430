package ca.polymtl.inf8480.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.nio.file.Files;
import java.nio.file.Path;

import ca.polymtl.inf8480.tp1.shared.FileModel;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Util;

public class Client {
	private final String clientPath = "src/ca/polymtl/inf8480/tp1/client";
	public static void main(String[] args) {
		String distantHostname = null;

		if (args.length > 0) {
			distantHostname = args[0];
		}

		Client client = new Client();
		client.run();
	}

	private ServerInterface serverStub = null;

	public Client() {
		super();

		serverStub = loadServerStub("127.0.0.1");

	}

	private void run() {
		while(true)
		{
			listenToInput();
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

	private void listenToInput() {
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
				if(!argument1.isEmpty())
				{
					try
					{
						result2 = serverStub.create(argument1);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						System.err.println("Erreur: " + e.getMessage());
					}
					System.out.println("Résultat appel create: " + result2);
				}
				else
				{
					System.out.println("Argument non-fourni.");
				}
				break;

			case "list":
				List<FileModel> list  = null;
				try {
					list = serverStub.list();
				} catch (RemoteException e) {
					System.err.println("Erreur: " + e.getMessage());
				}
				for (FileModel file : list) {
					System.out.println(file._fileName);
				}
				break;

			case "syncLocalDirectory":
				List<FileModel> serverList = null;
				try {
					serverList = serverStub.list();
				} catch (RemoteException e) {
					System.err.println("Erreur: " + e.getMessage());
				}
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

			case "get":
				if(!argument1.isEmpty())
				{
					try
					{
						File file = new File(clientPath + "/FileSystemClient/" + argument1 +".txt");
						String checksum = Util.getChecksum(file);

						byte[] filedata = serverStub.get(argument1, checksum);
         				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(clientPath + "/FileSystemClient/" + argument1 +".txt"));
         				output.write(filedata,0,filedata.length);
         				output.flush();
         				output.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						System.err.println("Erreur: " + e.getMessage());
					}
				}
				else
				{
					System.out.println("Argument non-fourni.");
				}
				break;
			case "push":
				if(!argument1.isEmpty())
				{
					try {
						File file = new File(clientPath +  "/FileSystemClient/" +  argument1 + ".txt");
						
						byte buffer[] = new byte[(int)file.length()];
						BufferedInputStream in = new
							BufferedInputStream(new FileInputStream(clientPath +  "/FileSystemClient/" +  argument1 + ".txt"));
						in.read(buffer,0,buffer.length);
						in.close();
						serverStub.push(argument1, buffer);
					} catch (Exception e) {
						//TODO: handle exception
						System.err.println("Erreur: " + e.getMessage());
					}
				} 
				else
				{
					System.out.println("Argument non-fourni.");
				}
				break;
				
			default:
				System.err.println("Erreur: commande non reconnue.");

		}
	}
}
