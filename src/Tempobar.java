
//Tempo slider
public class Tempobar {
	
	private static int myX = 204;
	private static int oldx = myX;

	public static void storeX(){
		oldx = myX;
	}
	
	//GETTERS:
	
	public static int getX(boolean relative){
		if(relative == true)
			return myX;
		
		else
			return myX + GUI.frame.getX();
	}
	
	public static int getOldX(){
		return oldx;
	}
	
	//SETTERS:
	
	public static void setX(int x){
		myX = x;
	}
	
}
