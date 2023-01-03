package script;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class gameScript {
	private String path;
	public File scriptFile;
	private String[] scriptLines = {
			"import Editor.Game \n",
			"import entities.ECS\n",
			"\n",
			"def start(ECS ecs, Game game) {\n",
			"	println 'Start World'\n",
			"}\n",
			"\n",
			"def tick(ECS ecs, Game game) {\n",
			"	println 'Tick'\n",
			"}\n",
			"\n",
			"this\n"
	};
	
	public gameScript(String path) throws IOException {
		this.setPath(path);
		this.scriptFile = new File(path);
		scriptFile.createNewFile();
	}

	public void setupScript() throws IOException {
		Writer writer2 = new FileWriter(path);
		

		for (String line : scriptLines) {
			writer2.write(line);
		}
		writer2.close();
	}
	
	public List<String> readScript() throws IOException {
		Scanner myReader = new Scanner(this.scriptFile);
		List<String> lines = new ArrayList<String>();
		while (myReader.hasNextLine()) {
			lines.add(myReader.nextLine());
		}
		myReader.close();
		return lines;
		
	}
	
	public void saveScript(String[] lines) throws IOException {
		Writer writer2 = new FileWriter(path);
		

		for (String line : lines) {
			writer2.write(line + "\n");
		}
		writer2.close();
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
