import java.awt.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Open {

	public static String filename = "";

	//When called, opens a file chooser
	public static void start(){

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Synth File", "synth");
		JFileChooser openFile = new JFileChooser();

		//Set file chooser properties
		openFile.setDialogTitle("Open");
		openFile.setApproveButtonText("Open");
		openFile.setFileFilter(filter);

		//If valid choice is made, run applyOpen with this file
		if(openFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			filename = openFile.getSelectedFile().getName();
			applyOpen(Open.parseFile(openFile.getCurrentDirectory().toString()));
		}

	}

	//When called, parses text of .synth file to the grid
	public static void applyOpen(String text){

		try{

			//Clear GUI note arraylist
			GUI.arr = new ArrayList<Note>();

			//Derive tempo from text
			int tempo = Integer.valueOf(text.substring(0,text.indexOf(":")));
			text = text.replace(text.substring(0,text.indexOf(":"))+":","");

			GUI.tempo.setTempo(tempo);
			Tempobar.setX(tempo+143);

			String[] stringArr = text.split(";");

			//Determine number of pages to add
			int highest = 0;

			for(String s:stringArr){
				String[] in = s.split(",");
				if(Integer.valueOf(in[0])>highest)
					highest = Integer.valueOf(in[0]);			
			}

			double result = highest/799.0;
			int pagesToAdd = (int) (Math.ceil(result));
			Sheet.setPages(0);

			for(int x = pagesToAdd;x>0;x--)
				Sheet.addPage();

			//Add notes based on text properties
			for(String s:stringArr){
				String[] in = s.split(",");

				Note n = Utils.findBox(new Point(17+Integer.valueOf(in[0])+
						GUI.frame.getLocation().x,17+Integer.valueOf(in[1])+
						GUI.frame.getLocation().y));

				n.setExists(true);
				n.setVolume(Double.valueOf(in[6]));
				n.setAccidental(in[3]);

				if(in[7].equals("true")){
					n.sustain.setSustain(true);

					if(in[8].equals("true"))
						n.sustain.makeHead();

					else if(in[9].equals("true"))
						n.sustain.makeTail();				
				}			
			}	

		}
		catch(Exception e){}

	}

	//Return textfile data as string
	public static String parseFile(String thePath){

		List<String> read = new ArrayList<String>();

		try {
			read = Files.readAllLines(Paths.get(thePath + "/" + filename),Charset.defaultCharset());
		} 
		catch (IOException e) {}

		String fix = "";

		for(String s:read)
			fix += s;

		return fix;
	}
}
