package ca.polymtl.inf8480.tp1.client;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import ca.polymtl.inf8480.tp1.shared.*;

public class FakeServer {
	int execute(int a, int b) {
		return a + b;
	}

	boolean create(String name) throws IOException {
		File file = new File("./FileSystem/" + name + ".txt");
		FileModel fileModel = new FileModel(name);
		List<FileModel> fileList = new ArrayList<FileModel>();

		// Fetch file list
		try
		{
			FileInputStream fileIn = new FileInputStream("./Save/fs.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			fileList = (List<FileModel>) in;
			in.close();
			fileIn.close();

			fileList.add(fileModel);

			// Save new file list
			FileOutputStream fileOut = new FileOutputStream("./Save/fs.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(fileList);
			out.close();
			fileOut.close();
		}
		catch (IOException e)
		{
			System.err.println("Erreur: " + e.getMessage());
		}
		
		return file.createNewFile();
	}

	List<FileModel> list()
	{
		List<FileModel> fileList = new ArrayList<FileModel>();
		try
		{
			FileInputStream fileIn = new FileInputStream("./Save/fs.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			fileList = (List<FileModel>) in;
			in.close();
			fileIn.close();
		}
		catch (IOException e)
		{
			System.err.println("Erreur: " + e.getMessage());
		}
		return fileList;
	}
}
