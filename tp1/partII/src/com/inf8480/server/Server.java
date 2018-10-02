
package com.inf8480.server;

import com.inf8480.shared.ServerInterface;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;

public class Server implements ServerInterface {
	private final String serverPath = "src/com/inf8480/server";

	public static void main(String[] args) {
//		System.setProperty("java.security.policy","`../../../../permissions.policy");
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

//	private AuthenticationService authenticationService = new AuthenticationService();

	private void run() {
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new SecurityManager());
//		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err
					.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	/*
	 * Méthode accessible par RMI. Additionne les deux nombres passés en
	 * paramètre.
	 */
	@Override
	public int execute(int a, int b) {
		return a + b;
	}

	@Override
	public boolean create(String name) throws IOException {
		File file = new File(serverPath + "/FileSystem/" + name + ".txt");
		return file.createNewFile();
	}

	@Override
	public void register(String username, String password) {
//        authenticationService.register(username, password
	}
}
