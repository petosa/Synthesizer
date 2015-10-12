import java.util.ArrayList;

//Class contained in each note which contains the
//sustain properties.
public class Sustain {

	//Variables
	private boolean isHead;
	private boolean isTail;
	private boolean linkedLeft;
	private boolean linkedRight;
	private boolean sustained;
	private Note n;

	//Constructor
	public Sustain(Note nu){
		n = nu;
	}

	//Search for a valid sustain member to the left
	public Note getAdjacentLeft(Note in){

		for(Note note:GUI.arr){

			if(note.getRow().equals(in.getRow()) //Same row
					&&note.getExists() //Note in question exists
					&&in.getExists() //Linked note exists
					&&!note.equals(in) //Not linked note
					&&note.getAccidental().equals(n.getAccidental()) //Accidentals match
					&&note.getVolume() == n.getVolume() //Volumes match
					&&note.getX(true) == in.getX(true) - 17 //Immediately to left
					)

				return note;
		}	
		return null;

	}

	//Search for a valid sustain member to the right
	public Note getAdjacentRight(Note in){

		for(Note note:GUI.arr){

			if(note.getRow().equals(in.getRow()) //Same row
					&&note.getExists() //Note in question exists
					&&in.getExists() //Linked note exists
					&&!note.equals(in) //Not linked note
					&&note.getAccidental().equals(n.getAccidental()) //Accidentals match
					&&note.getVolume() == n.getVolume() //Volumes match
					&&note.getX(true) == in.getX(true) + 17 //Immediately to right
					)

				return note;

		}
		return null;

	}

	//Recursively find size of sustained note and interact
	//with the sustain object with other notes to establish
	//a sustain chain
	public void convertAdjacent(int mode){

		linkedLeft = false;
		linkedRight = false;

		//If there's a note to the right, set this sustain
		//object's properties, that sustain object's properties,
		//and tell that note to look for more notes on the right.
		if(getAdjacentRight(n)!=null){

			Note adjR = getAdjacentRight(n);
			linkedRight = true;

			if(mode == 1||mode == 0)
				adjR.sustain.convertAdjacent(1);

		}

		//If there's a note to the left, set this sustain
		//object's properties, that sustain object's properties,
		//and tell that note to look for more notes on the left.
		if(getAdjacentLeft(n)!=null){

			Note adjL = getAdjacentLeft(n);
			linkedLeft = true;

			if(mode == 2||mode == 0)
				adjL.sustain.convertAdjacent(2);

		}

		//Set flags based off of results.

		if(linkedRight && !linkedLeft){
			sustained = true;			
			isHead = true;
		}

		else if(!linkedRight && linkedLeft){
			sustained = true;			
			isTail = true;
		}

		else if(linkedRight && linkedLeft){
			sustained = true;
			isTail = false;
			isHead = false;
		}

	}

	//When part of a sustained note is deleted, it splits into multiple sustained notes or
	//becomes a shorter sustained note
	public void sustainDelete(Note nu){

		//If note is sustained...
		if(nu.sustain.getSustain()){

			Note nuL = nu.sustain.getAdjacentLeft(nu);
			Note nuR = nu.sustain.getAdjacentRight(nu);

			//Determine how to split note based off of its adjacent sustained notes

			if(!nu.sustain.getHead() && !nuL.sustain.getHead())
				nuL.sustain.makeTail();

			if(!nu.sustain.getTail() && !nuR.sustain.getTail())
				nuR.sustain.makeHead();

			if(!nu.sustain.getHead() && nuL.sustain.getHead())
				nuL.sustain.clearData();

			if(!nu.sustain.getTail() && nuR.sustain.getTail())
				nuR.sustain.clearData();

			if(nu.sustain.getHead())
				nuR.sustain.makeHead();

			if(nu.sustain.getTail())
				nuL.sustain.makeTail();

		}

	}

	//Sustain splitter: Shift + Right Click splits a sustain
	public void sustainBreak(Note nu){

		//If note is sustained...
		if(nu.sustain.getSustain()){

			Note nuL = nu.sustain.getAdjacentLeft(nu);

			//Determine how to split note based off of its adjacent sustained notes

			if(!nu.sustain.getHead() && nuL != null && !nuL.sustain.getHead() && !nu.sustain.getTail()){
				nuL.sustain.makeTail();
				nu.sustain.makeHead();
			}

			else if(!nu.sustain.getHead() && nuL != null && nuL.sustain.getHead() && !nu.sustain.getTail()){
				nuL.sustain.clearData();
				nu.sustain.makeHead();
			}

			else if(nu.sustain.getTail() && nuL != null && nuL.sustain.getHead()){
				nuL.sustain.clearData();
				nu.sustain.clearData();
			}

			else if(nu.sustain.getTail() && nuL != null && !nuL.sustain.getHead()){
				nuL.sustain.makeTail();
				nu.sustain.clearData();
			}

		}

	}

	//Given head, return integer distance in notes to tail
	public int distanceToTail(Note head){

		int count = 0;
		Note nuR = head;
		boolean foundTail = false;

		//Iterate right through a sustained note chain to find the head		
		while(!foundTail)		
			if(nuR != null)
				if(nuR != null&&nuR.sustain.getSustain()){
					count++;

					if(nuR.sustain.getTail())
						foundTail = true;

					else
						nuR = nuR.sustain.getAdjacentRight(nuR);
				}

		//Return count to tail
		return count;
	}

	//When called, cycles through the accidental of all members of this sustained note chain
	public void changeSustainAccidental(Note nu){

		if(nu.sustain.getSustain()){

			//Boolean flags
			boolean foundHead = false;
			boolean foundTail = false;

			//Define notes
			Note nuL = nu;
			Note nuR = nu.sustain.getAdjacentRight(nu);

			//Case: Clicked on tail of sustain chain
			if(nu.sustain.getTail()){
				nuR = nu;
				nuL = nu.sustain.getAdjacentLeft(nu);
			}

			//Lists of notes on either side
			ArrayList<Note> toLeft = new ArrayList<Note>();
			ArrayList<Note> toRight = new ArrayList<Note>();

			//Find the head of this sustain chain
			while(!foundHead){

				//Set nuL to head of sustain chain
				if(nuL != null)
					if(nuL != null && nuL.sustain.getSustain()){
						toLeft.add(nuL);

						if(nuL.sustain.getHead())
							foundHead = true;

						else
							nuL = nuL.sustain.getAdjacentLeft(nuL);
					}

			}

			//Find the tail of this sustain chain			
			while(!foundTail){	

				//Set nuR to tail of sustain chain
				if(nuR != null)
					if(nuR != null && nuR.sustain.getSustain()){
						toRight.add(nuR);

						if(nuR.sustain.getTail())
							foundTail = true;

						else
							nuR = nuR.sustain.getAdjacentRight(nuR);
					}

			}

			//Cycle accidental for all sustained chained notes to the left
			for(Note no:toLeft){

				if(no.getAccidental().equals("natural"))
					no.setAccidental("sharp");

				else if(no.getAccidental().equals("sharp"))
					no.setAccidental("flat");

				else if(no.getAccidental().equals("flat"))
					no.setAccidental("natural");

			}

			//Cycle accidental for all sustained chained notes to the right
			for(Note no:toRight){

				if(no.getAccidental().equals("natural"))
					no.setAccidental("sharp");

				else if(no.getAccidental().equals("sharp"))
					no.setAccidental("flat");

				else if(no.getAccidental().equals("flat"))
					no.setAccidental("natural");

			}

		}

	}

	//When called, sets the volume of all members of this sustained note chain
	public void setSustainVolume(Note nu,double v){

		if(nu.sustain.getSustain()){

			//Boolean flags
			boolean foundHead = false;
			boolean foundTail = false;

			//Define notes
			Note nuL = nu;
			Note nuR = nu.sustain.getAdjacentRight(nu);

			//Case: Clicked on tail of sustain chain
			if(nu.sustain.getTail()){
				nuR = nu;
				nuL = nu.sustain.getAdjacentLeft(nu);
			}

			//Lists of notes on either side
			ArrayList<Note> toLeft = new ArrayList<Note>();
			ArrayList<Note> toRight = new ArrayList<Note>();

			//Find the head of this sustain chain
			while(!foundHead)
				
				//Set nuL to head of sustain chain	
				if(nuL != null)
					if(nuL != null && nuL.sustain.getSustain()){
						toLeft.add(nuL);
						
						if(nuL.sustain.getHead())
							foundHead = true;
						
						else
							nuL = nuL.sustain.getAdjacentLeft(nuL);
					}
			
			//Find the tail of this sustain chain
			while(!foundTail){		
				
				//Set nuR to tail of sustain chain
				if(nuR != null)
					if(nuR != null && nuR.sustain.getSustain()){
						toRight.add(nuR);
						
						if(nuR.sustain.getTail())
							foundTail = true;
						
						else
							nuR = nuR.sustain.getAdjacentRight(nuR);
					}
			}

			//Change volume for all sustained chained notes to the left
			for(Note no:toLeft)
				no.setVolume(v);

			//Change volume for all sustained chained notes to the right
			for(Note no:toRight)
				no.setVolume(v);		

		}

	}

	//Convert a note into a head
	public void makeHead(){
		linkedLeft = false;
		isTail = false;
		isHead = true;
	}

	//Convert a note into a tail
	public void makeTail(){
		linkedRight = false;
		isTail = true;
		isHead = false;
	}

	//Remove sustain from note
	public void clearData(){
		n.sustain = new Sustain(n);
	}
	
	//GETTERS:
	
	public boolean getHead(){
		return isHead;
	}
		
	public boolean getTail(){
		return isTail;
	}
 
	public boolean getSustain(){
		return sustained;
	}

	//SETTERS:
	
	public void setSustain(Boolean bool){
		sustained = bool;
	}

}



