package ca.polymtl.inf8480.tp1.client;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import ca.polymtl.inf8480.tp1.shared.*;

public class FakeServer {
	private final String clientPath = "src/ca/polymtl/inf8480/tp1/client";

	int execute(int a, int b) {
		return a + b;
	}

	boolean create(String name) throws IOException {
		File file = new File( clientPath + "/FileSystemServer/" + name + ".txt");
		FileModel fileModel = new FileModel(name);
		List<FileModel> fileList = new ArrayList<FileModel>();
		boolean isInFile = true;
		
		fileList = LoadFilesFromSave();

		fileList.add(fileModel);
		
		SaveFileList(fileList);

		return file.createNewFile();
	}

	List<FileModel> list()
	{
		return LoadFilesFromSave();
	}

	List<FileModel> LoadFilesFromSave()
	{
		List<FileModel> fileList = new ArrayList<FileModel>();
		try
		{
			FileInputStream fileIn = new FileInputStream(clientPath + "/Save/fs.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try
			{
				fileList = (List<FileModel>) in.readObject();
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
		return fileList;
	}

	void SaveFileList(List<FileModel> list)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(clientPath + "/Save/fs.ser");
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

	void syncLocaDirectory()
	{

	}
}
