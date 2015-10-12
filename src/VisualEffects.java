import java.awt.Color;
import java.awt.Point;

//Handles colors and images
public class VisualEffects {

	//Color variables
	private static Color exitButtonColor;
	private static Color minimizeButtonColor;
	private static Color boxOff1,boxOff2,boxOff3,boxOff4,boxOff5,boxOff6;

	//Causes GUI elements to light up when the mouse hovers over them
	public static void reactToHover(Point m,Point f){

		//Exit Button
		if(Utils.isIn("exitButton",m,f))
			exitButtonColor = new Color(255,1,20,120);
		
		else
			exitButtonColor = new Color(255,1,1,100);

		//Minimize Button
		if(Utils.isIn("minimizeButton",m,f))
			minimizeButtonColor = new Color(255,145,1,120);
		
		else
			minimizeButtonColor = new Color(255,125,1,100);

		//Add Page Button
		if(Utils.isIn("addPage",m,f))
			boxOff1 = new Color(255,145,0,100);
		
		else
			boxOff1 = new Color(2,222,1,0);
		
		//Play Button
		if(Utils.isIn("play",m,f))
			boxOff2 = new Color(255,145,0,100);
		
		else
			boxOff2 = new Color(2,222,1,0);
		
		//Pause Button
		if(Utils.isIn("pause",m,f))
			boxOff3 = new Color(255,145,0,100);
		
		else
			boxOff3 = new Color(2,222,1,0);
		
		//Rewind Button
		if(Utils.isIn("rewind",m,f))
			boxOff4 = new Color(255,145,0,100);
		
		else
			boxOff4 = new Color(2,222,1,0);
		
		//Save Button
		if(Utils.isIn("save",m,f))
			boxOff5 = new Color(255,145,0,100);
		
		else
			boxOff5 = new Color(2,222,1,0);
		
		//Open Button
		if(Utils.isIn("open",m,f))
			boxOff6 = new Color(255,145,0,100);
		
		else
			boxOff6 = new Color(2,222,1,0);

		try{
			
			//Highlight rows when they are hovered over in edit mode.
			if(GUI.editmode){			
				String row = Utils.findBox(m).getRow();
				
				for(Note n:GUI.arr){
					if(n.getRow().equals(row) && n.getX(true) > 0 && n.getX(true) < GUI.dim.getWidth())
						n.setColor(getColor("highlight"));
					
					else
						n.setColor(getColor("grid"));
				}
				
			}	
		}catch(Exception e){		
			for(Note n:GUI.arr)
				n.setColor(getColor("grid"));	
			
		}
	}

	//Given atring, return color object
	public static Color getColor(String type){
		
		//Exit Button Color
		if(type.equals("exitButton"))
			return exitButtonColor;
		
		//Minimize Button Color
		if(type.equals("minimizeButton"))
			return minimizeButtonColor;
		
		//Grid Color
		if(type.equals("grid"))
			return new Color(255,255,255,10);
		
		//Sidebar Color
		if(type.equals("sidebar"))
			return new Color(255,145,0,100);
		
		//Highlight Color
		if(type.equals("highlight"))
			return new Color(255,255,255,20);
		
		//GUI Glow for Add Page Button
		if(type.equals("boxOffPage"))
			return boxOff1;
		
		//GUI Glow for Play Button
		if(type.equals("boxOffPlay"))
			return boxOff2;
		
		//GUI Glow for Pause Button
		if(type.equals("boxOffPause"))
			return boxOff3;
		
		//GUI Glow for Rewind Button
		if(type.equals("boxOffRewind"))
			return boxOff4;
		
		//GUI Glow for Save Button
		if(type.equals("boxOffSave"))
			return boxOff5;
		
		//GUI Glow for Open Button
		if(type.equals("boxOffOpen"))
			return boxOff6;

		else return null;
	}
	
}
