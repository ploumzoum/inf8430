

package ca.polymtl.inf8480.tp1.server;

import ca.polymtl.inf8480.tp1.shared.FileModel;
import ca.polymtl.inf8480.tp1.shared.LockAnswer;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Util;
import ca.polymtl.inf8480.tp1.shared.AuthenticationServerInterface;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements ServerInterface {
	private final String serverPath = "src/ca/polymtl/inf8480/tp1/server";
	private AuthenticationServerInterface authenticationService;
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
			try {
				authenticationService = (AuthenticationServerInterface) Naming.lookup("authenticationServer");
			} catch(NotBoundException e) {
				System.err.println("Erreur: " + e.getMessage());
			}
			
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
		File file = new File( serverPath + "/FileSystem/" + name + ".txt");
		FileModel fileModel = new FileModel(name);
		List<FileModel> fileList;
		boolean isInFile = true;

		fileList = LoadFilesFromSave();

		fileList.add(fileModel);

		SaveFileList(fileList);

		return file.createNewFile();
	}

	void SaveFileList(List<FileModel> list)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(serverPath + "/Save/fs.ser");
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
	public void register(String username, String password) {
//        authenticationService.register(username, password
	}

	@Override
	public List<FileModel> list() {
		return LoadFilesFromSave();
	}

	List<FileModel> LoadFilesFromSave()
	{
		List<FileModel> fileList = new ArrayList<FileModel>();
		fileList = getFileModels(fileList, serverPath);
		return fileList;
	}

	public static List<FileModel> getFileModels(List<FileModel> fileList, String serverPath) {
		try
		{
			FileInputStream fileIn = new FileInputStream(serverPath + "/Save/fs.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try
			{
				fileList = (List<FileModel>) in.readObject();
			}
			catch (ClassNotFoundException e)
			{
				System.err.println("File list error: "+e.getMessage());
			}
			in.close();
			fileIn.close();
		}
		catch (IOException e)
		{
			System.err.println("File list error: "+e.getMessage());
		}
		return fileList;
	}

	// Tiré de https://www.oracle.com/technetwork/articles/javase/rmi-corba-136641.html
	@ Override
	public byte[] get(String fileName, String clientChecksum){
		try {
			File file = new File(serverPath +  "/FileSystem/" +  fileName + ".txt");
			String serverChecksum = Util.getChecksum(file);

			if(clientChecksum == null || !clientChecksum.equals(serverChecksum))
			{
				byte buffer[] = new byte[(int)file.length()];
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(serverPath +  "/FileSystem/" +  fileName + ".txt"));
				input.read(buffer,0,buffer.length);
				input.close();
				return(buffer);
			}
			else
			{
				return null;
			}
		} catch(Exception e){
			System.err.println("File download error: "+e.getMessage());
			return(null);
		}
	}

	@Override
	public LockAnswer lock(String filename, String checksum)
	{
		try
		{
			List<FileModel> fileList = new ArrayList<FileModel>();
			fileList = getFileModels(fileList, serverPath);
			boolean locked = true;
			String lockUID = "";
			for (FileModel model : fileList)
			{
				if(model._fileName.equals(filename))
				{
					locked = model._locked;
					lockUID = model._lockUID;
				}
			}
			if(!locked)
			{
				// Lock the file
				for (FileModel model : fileList)
				{
					if(model._fileName.equals(filename))
					{
						model._locked = true;
						model._lockUID = ""; // TODO: Put client id here
					}
				}
				return new LockAnswer(get(filename, checksum), true, "");
			}
			else
			{
				return new LockAnswer(null, false, lockUID);	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new LockAnswer(null, false, "");
		}
	}


	@ Override
	public void push(String filename, byte[] content) {
		try {
			byte[] filedata = content;
			System.err.println(""+filename+content.length);
         	File file = new File(serverPath + "/FileSystem/" + filename +"txt");
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(serverPath + "/FileSystem/" + filename +"txt"));
         	output.write(filedata,0,filedata.length);
			output.flush();
			output.close();
		} catch (Exception e) {
			System.err.println("File upload error: "+e.getMessage());
		}
	}	
}
