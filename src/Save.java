import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Save {

	//Default filename
	public static String filename = "MyProject";

	//When called, opens a file chooser
	public static void start() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Synth File", ".synth");
		JFileChooser saveFile = new JFileChooser();
		
		//Set file chooser properties
		saveFile.setDialogTitle("Save");
		saveFile.setApproveButtonText("Save");
		saveFile.setSelectedFile(new File(filename));
		saveFile.setFileFilter(filter);
		
		//If valid choice is made, run fileWrite with this file
		if(saveFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			filename = saveFile.getSelectedFile().getName();
			fileWrite(saveFile.getCurrentDirectory().toString());
		}
		
	}
	
	//Create output based on contents of grid
	public static String deriveExport(int tempo,ArrayList<Note> arr){

		String output = "";
		output += tempo + ":";

		//Record all existing note data
		for(Note n:arr){
			if(n.getExists()){
				output+=(n.getX(true))  + ",";
				output+=(n.getY(true)) + ",";
				output+=n.getNote() + ",";
				output+=n.getAccidental() + ",";
				output+=n.getLength() + ",";
				output+=n.getOctave() + ",";
				output+=n.getVolume() + ",";
				output+=n.sustain.getSustain() + ",";
				output+=n.sustain.getHead() + ",";
				output+=n.sustain.getTail() + ";";
			}
		}

		return output;
	}

	//Write save output as a .synth file
	public static void fileWrite(String s) {

		try {
			//Get output
			String content = deriveExport(GUI.tempo.getTempo(),GUI.arr);

			File file = new File(s + "/" + filename + ".synth");

			//If file does not exist, create it
			if (!file.exists()) {
				file.createNewFile();
			}

			//Write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
		}
		catch (IOException e){}

	}
	
}



