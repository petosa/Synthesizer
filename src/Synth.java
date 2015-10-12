
import javax.sound.sampled.LineUnavailableException;

//Wrapper class for Beep.java
public class Synth extends Thread{

	//Frequencies corresponding to specific notes
	private final double C_NATURAL = 16.35;
	private final double C_SHARP = 17.32;
	private final double D_NATURAL = 18.35;
	private final double E_FLAT = 19.45;
	private final double E_NATURAL = 20.60;
	private final double F_NATURAL = 21.83;
	private final double F_SHARP = 23.12;
	private final double G_NATURAL = 24.50;
	private final double G_SHARP = 25.96;
	private final double A_NATURAL = 27.50;
	private final double B_FLAT = 29.14;
	private final double B_NATURAL = 30.87;

	//Array of note letters
	private final char[] notes = {'a','b','c','d','e','f','g'};

	//Properties of tone
	private int octave;
	private int length;
	private char note;
	private double volume = 1;
	private String accidental;

	//Constructor
	public Synth(char n, String a, int l, int o, double v){	
		note = n;
		accidental = a;
		octave = o;
		volume = v;
		length = l;
	}

	//Play the constructed note
	public void run() {
		
		//If this is a valid note
		if(new String(notes).contains(note+"")){

			try {
				//Switch statement for each note type
				switch(note){
				
				//C
				case 'c':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(C_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(C_SHARP,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(B_NATURAL,true),length,volume);				
					break;
					
				//D
				case 'd':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(D_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(E_FLAT,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(C_SHARP,false),length,volume);					
					break;
								
				//E
				case 'e':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(E_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(F_NATURAL,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(E_FLAT,false),length,volume);					
					break;
				
				//F	
				case 'f':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(F_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(F_SHARP,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(E_NATURAL,false),length,volume);
					break;
				
				//G	
				case 'g':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(G_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(G_SHARP,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(F_SHARP,false),length,volume);			
					break;
				
				//A
				case 'a':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(A_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(B_FLAT,false),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(G_SHARP,false),length,volume);			
					break;
				
				//B	
				case 'b':
					if(accidental.equals("natural"))
						Beep.tone(deriveHertz(B_NATURAL,false),length,volume);
					
					if(accidental.equals("sharp"))
						Beep.tone(deriveHertz(C_NATURAL,true),length,volume);
					
					if(accidental.equals("flat"))
						Beep.tone(deriveHertz(B_FLAT,false),length,volume);			
					break;
					
				}
			} catch (LineUnavailableException e) {}
		}

	}

	//Adjust based on octave
	public int deriveHertz(double myHertz,boolean special){
		
		int octaveOrig = octave;
		
		//Case for octave shift
		if(special){		
			if(myHertz == B_NATURAL)
				octave -= 1;
			
			else if(myHertz == C_NATURAL)
				octave += 1;		
		}

		int answer = (int) Math.round(myHertz * Math.pow(2, octave));
		octave = octaveOrig;
		
		return answer;
	}

}
