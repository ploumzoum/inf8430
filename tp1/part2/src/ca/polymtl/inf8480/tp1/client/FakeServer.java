package ca.polymtl.inf8480.tp1.client;
import java.io.File;
import java.io.IOException;

public class FakeServer {
	int execute(int a, int b) {
		return a + b;
	}

	boolean create(String name) throws IOException {
		File file = new File("./FileSystem/" + name + ".txt");
		return file.createNewFile();
	}
}
