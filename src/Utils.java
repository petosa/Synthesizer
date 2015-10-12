import java.awt.Point;

//Miscellaneous utilities used by the program
public class Utils {

	//Returns whether a mouse point is within a specified area
	public static boolean isIn(String loc,Point mousePos,Point framePos){

		//Exit Button
		if(loc.equals("exitButton"))
			if((mousePos.x >= framePos.getLocation().x + GUI.dim.getWidth() - 43 && mousePos.x <= framePos.getLocation().x+GUI.dim.getWidth()-3)
					&&(mousePos.y <= framePos.getLocation().y+20 && mousePos.y >= framePos.getLocation().y))
				return true;

		//Minimize Button
		if(loc.equals("minimizeButton"))
			if((mousePos.x >= framePos.getLocation().x + GUI.dim.getWidth() - 83 && mousePos.x<framePos.getLocation().x+GUI.dim.getWidth() - 43)
					&&(mousePos.y <= framePos.getLocation().y+20 && mousePos.y >= framePos.getLocation().y))
				return true;

		//Title Bar
		if(loc.equals("titleBar"))
			if((mousePos.x >= framePos.getLocation().x && mousePos.x <= framePos.getLocation().x+720)
					&&(mousePos.y <= framePos.getLocation().y+20 && mousePos.y >= framePos.getLocation().y))
				return true;
		
		//Grid Tile
		if(loc.equals("box"))
			for(Note n:GUI.arr)
				if((mousePos.x>=n.getX(false) && mousePos.x<=n.getX(false)+17)
						&&(mousePos.y<=n.getY(false)+17 && mousePos.y >= n.getY(false)))
					return true;

		//Add Page Button
		if(loc.equals("addPage"))
			if((mousePos.x >= framePos.getLocation().x + 663 && mousePos.x <= framePos.getLocation().x+705)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		//Rewind Button
		if(loc.equals("rewind"))
			if((mousePos.x >= framePos.getLocation().x + 5 && mousePos.x <= framePos.getLocation().x+47)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		//Pause button
		if(loc.equals("pause"))
			if((mousePos.x >= framePos.getLocation().x + 48 && mousePos.x <= framePos.getLocation().x+89)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		//Play Button
		if(loc.equals("play"))
			if((mousePos.x >= framePos.getLocation().x + 90 && mousePos.x <= framePos.getLocation().x+132)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		//Scrollbar
		if(loc.equals("scrollbar")&&Scrollbar.getVisible())
			if((mousePos.x >= framePos.getLocation().x + Scrollbar.getX(true) && mousePos.x <= framePos.getLocation().x+Scrollbar.getX(true)+Scrollbar.getLength())
					&&(mousePos.y<framePos.getLocation().y+80 && mousePos.y > framePos.getLocation().y+63))
				return true;
			
		//Tempo slider
		if(loc.equals("tempobar"))
			if((mousePos.x >= framePos.getLocation().x + Tempobar.getX(true) && mousePos.x <= framePos.getLocation().x+Tempobar.getX(true)+19)
					&&(mousePos.y<framePos.getLocation().y+59 && mousePos.y > framePos.getLocation().y+24))
				return true;
			
		//Save Button
		if(loc.equals("save"))
			if((mousePos.x >= framePos.getLocation().x + 707 && mousePos.x <= framePos.getLocation().x+749)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		//Open Button
		if(loc.equals("open"))
			if((mousePos.x >= framePos.getLocation().x + 751 && mousePos.x <= framePos.getLocation().x+793)
					&&(mousePos.y <= framePos.getLocation().y+61 && mousePos.y >= framePos.getLocation().y+21))
				return true;

		return false;
	}

	//Given mouse coordinates, find the note that is being hovered over
	public static Note findBox(Point mousePos){
		for(Note n:GUI.arr)
			if((mousePos.x >= n.getX(false) && mousePos.x <= n.getX(false)+17)
					&&(mousePos.y <= n.getY(false)+17 && mousePos.y >= n.getY(false)))
				return n;

		return null;
	}

	//Called following a playback, resets all notes
	public static void rebuildSheet(){
		for(Note n:GUI.arr){
			n.setVisible(true);
			n.setPlayed(false);	
		}
	}
	
	//Move all notes to specified offset
	public static void sync(int offset){
		for(Note n:GUI.arr){
			n.setX(GUI.frame.getLocation().x + n.getOrig() - offset);				
		}
	}

	//Silence any sounds from sustained notes or otherwise when playback is stopped
	public static void stopSounds(){
		if(GUI.notesPlaying>0)
			GUI.silence = true;
	}

	//Called when user pauses a playback
	public static void pause(){

		//Start editmode
		GUI.editmode = true;

		//Reset notes
		Utils.rebuildSheet();

		//If the first page hasn't passed, move all notes to starting positions
		if(Sheet.getPages() == 1)
			Utils.sync(0);

		//**MATH
		//Calculates where to place slider after pause
		
		int totalScrollDistance = (int) (GUI.dim.getWidth()-Scrollbar.getLength());
		int totalPixels = (int) ((Sheet.getPages()-1)*GUI.dim.getWidth()-1);

		if(Sheet.getPages() > 1){
		
			int theX = 0;
			
			for(Note n:GUI.arr)
				if(n.getX(true)>=16&&n.getX(true)<=33)
					theX = n.getX(false);

			int totalPixelsSoFar = theX-GUI.frame.getLocation().x-16+GUI.currentX;
			int magicnumber = (totalPixelsSoFar*totalScrollDistance/totalPixels)-(theX-GUI.frame.getLocation().x);

			if(magicnumber>0 &&	magicnumber<GUI.dim.getWidth() - Scrollbar.getLength())
				Scrollbar.setX(magicnumber);
			
			else if (magicnumber < 0)
				Scrollbar.setX(0);
			
			else if (magicnumber > GUI.dim.getWidth() - Scrollbar.getLength())
				Scrollbar.setX((int) GUI.dim.getWidth() - Scrollbar.getLength());
		}

		Utils.stopSounds();

	}

	//Changes length of all notes based on current tempo
	public static void adjustLength(){
		for(Note n:GUI.arr)
			n.setLength((int) (1000000*(1/(1000.0*GUI.tempo.getTempo()/60))/4));
	}


}


