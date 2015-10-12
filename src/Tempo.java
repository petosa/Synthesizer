import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

//Timer for playback
public class Tempo {

	private int bpm;
	private Timer t;

	//Constructor
	public Tempo(int b){
		bpm = b;
	}

	//Start a tempo by creating a task which ticks at a specified rate
	public void tick(){
		
		int mult = 1;
		if(bpm>100)
			mult = 2;

		//Create task
		t = new Timer(15000000/(bpm/mult*1000)/16,new ActionListener(){		
			
			public void actionPerformed(ActionEvent evt) {
				if(!GUI.editmode){
					int mult = 1;
					if(bpm>100)
						mult = 2;
					for(Note n:GUI.arr)
						n.setX(n.getX(false)-mult);					
				}  				
			}
		}
				);	
		
		t.start();
		
	}
	
	//Adjust bpm
	public void setTempo(int in){
		bpm = in;
		t.stop();
		GUI.tempo.tick();
	}
	
	//Return current tempo
	public int getTempo(){
		return bpm;
	}
	
	//Calculate time needed for a certain number of notes to pass
	//at this tempo
	public int deriveTimeForPixels(int x){
		return (int) ((60000*(x/64.0)/bpm)-(10/bpm * 125));
	}
}
