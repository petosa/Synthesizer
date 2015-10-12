import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

//The Note class. Every instance of this object represents a box on the grid.
public class Note {

	//Create a sustain object for this note
	public Sustain sustain = new Sustain(this);
	
	//Integers that store properties of this note
	private int origX;
	private int myX;
	private int myY;
	private int length;
	private int octave;
	
	//Booleans that store properties of this note
	private boolean played = false;
	private boolean exists = false;
	private boolean visible = true;
	
	//Misc variables
	private float alpha;
	private char note;
	private double volume;
	
	//Variables storing accidental and note tone
	private String accidental;
	private Synth mySynth;

	private Color col = VisualEffects.getColor("grid");

	//Constructor
	public Note(int x, int y, char n, String a, int l, int o, double v){
		
		//Variable assignment
		origX = x;
		myX = x;
		myY = y;
		length = l;
		octave = o;
		note = n;
		volume = v;
		accidental = a;
		mySynth = new Synth(n,a,l,o,v);	
		
	}

	//When called, plays this note. Called during playback.
	public void play(){

		//Run if this note hasn't been played, it exists, and 
		//the user is not editing
		if(!played && exists &&! GUI.editmode){
			
			//Case: Regular note
			if(!sustain.getSustain()){
				played = true;
				mySynth = new Synth(note,accidental,length,octave,volume);
				mySynth.start();
			}
			
			//Case: Body of sustained note
			else if(sustain.getSustain() && !sustain.getHead())
				played = true;
			
			//Case: Head of sustained note
			else if(sustain.getSustain() && sustain.getHead()){				
				played = true;						
				length = GUI.tempo.deriveTimeForPixels(17*sustain.distanceToTail(this));
				mySynth = new Synth(note,accidental,length,octave,volume);
				mySynth.start();
			}
			
		}

	}

	//When called, plays this note. Called during editing.
	public void test(){

		if(exists){
			
			//Case: Regular note
			if(!sustain.getSustain()){
				mySynth = new Synth(note,accidental,150,octave,volume);
				mySynth.start();
			}
			
			//Case: Sustained note
			else if(sustain.getSustain()){
				mySynth = new Synth(note,accidental,150,octave,volume);
				mySynth.start();
			}	
			
		}
		
	}

	//When called, paints note as box and/or image
	public void paint(Graphics g){
		
		//If note box is visible
		if(visible){
			
			//Draw grid box
			g.setColor(col);
			g.fillRect(myX,myY,16,16);

			//Draw sustained note components
			
			//Case: Sustained natural head
			if(sustain.getSustain() && accidental.equals("natural") && exists && sustain.getHead()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("head.png")), myX-1, myY-1,17,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained natural tail
			else if(sustain.getSustain() && accidental.equals("natural") && exists && sustain.getTail()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("tail.png")), myX-1, myY-1,18,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained natural body
			else if(sustain.getSustain() && accidental.equals("natural") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("body.png")), myX-1, myY-1,17,17,null);			
				setAlpha(g,1f);
			}
			
			//Case: Natural note
			else if(accidental.equals("natural") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("dot.png")), myX-1, (myY-1),17,17,null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained sharp head
			else if(sustain.getSustain() && accidental.equals("sharp") && exists && sustain.getHead()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("headsharp.png")), myX-1, myY-1,17,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained sharp tail
			else if(sustain.getSustain() && accidental.equals("sharp") && exists && sustain.getTail()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("tailsharp.png")), myX-1, myY-1,18,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained sharp body
			else if(sustain.getSustain() && accidental.equals("sharp") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("bodysharp.png")), myX-1, myY-1,17,17,null);
				setAlpha(g,1f);
			}
			
			//Case: Sharp note
			else if(accidental.equals("sharp") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("dotsharp.png")), myX-1, myY-1,17,17, null);
				setAlpha(g,1f);
			}

			//Case: Sustained flat head
			else if(sustain.getSustain() && accidental.equals("flat") && exists && sustain.getHead()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("headflat.png")), myX-1, myY-1,17,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained flat tail
			else if(sustain.getSustain() && accidental.equals("flat") && exists && sustain.getTail()){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("tailflat.png")), myX-1, myY-1,18,17, null);
				setAlpha(g,1f);
			}
			
			//Case: Sustained flat body
			else if(sustain.getSustain() && accidental.equals("flat") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("bodyflat.png")), myX-1, myY-1,17,17,null);
				setAlpha(g,1f);
			}
			
			//Case: Flat note
			else if(accidental.equals("flat") && exists){
				setAlpha(g,(float)volume);
				g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("dotflat.png")), myX-1, myY-1,17,17, null);
				setAlpha(g,1f);
			}
		}
	}

	//GETTERS:
	
	public boolean getExists(){
		return exists;
	}
	
	public int getX(boolean relative){
		if(relative == true)
			return myX;
		else
			return myX + GUI.frame.getX();
	}

	public int getY(boolean relative){
		if(relative == true)
			return myY;
		else
			return myY + GUI.frame.getY();
	}
	
	public double getVolume(){
		return volume;
	}
	
	public int getOctave(){
		return octave;
	}
	
	public int getOrig(){
		return origX;
	}
	
	public char getNote(){
		return note;
	}
	
	public boolean getPlayed() {
		return played;
	}
	
	public String getRow(){
		return note + "" + octave;
	}
	
	public int getLength() {
		return length;
	}
	
	//SETTERS:
	
	private void setAlpha(Graphics g,float f){
		alpha = f;
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));	
	}

	public void setX(int newX){
		myX = newX- GUI.frame.getLocation().x;
	}

	public void setY(int newY){
		myY = newY- GUI.frame.getLocation().y;
	}

	public void setPlayed(boolean status){
		played = status;
	}

	public void setTempo(int t){
		length = t;
	}

	public void setExists(boolean status){
		exists = status;
	}	

	public void setColor(Color c){
		col = c;
	}

	public String getAccidental(){
		return accidental;
	}

	public void setVolume(double v){
		volume = v;
	}

	public void setVisible(boolean b){
		visible = b;
	}

	public void setAccidental(String a){
		accidental = a;
		mySynth = new Synth(note,accidental,length,octave,volume);
	}

	public void setLength(int len) {
		length = len;	
	}

}
