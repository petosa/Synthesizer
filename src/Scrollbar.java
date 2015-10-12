
//Page scrollbar class
public class Scrollbar {

	//Variables
	private static boolean visible = false;	
	private static int pages = 0;
	private static int length = (int) GUI.dim.getWidth();
	private static int myX = 0;
	private static int oldX = myX;

	public static void incPages(){
		pages++;
	}
	public static void storeX(){
		oldX = myX;
	}
	
	//GETTERS:

	public static boolean getVisible(){
		return visible;
	}

	public static int getPages(){
		return pages;
	}

	public static int getLength(){	
		try{
			length = (int) GUI.dim.getWidth()/Sheet.getPages();
		}
		catch(Exception e){}

		return length;
	}

	public static int getX(boolean relative){
		if(relative == true)
			return myX;
		else
			return myX + GUI.frame.getX();
	}
	
	public static int getOldX(){
		return oldX;
	}
	
	public static double getCompletion(){
		return myX/(GUI.dim.getWidth()-getLength());
	}

	public static double getCompletion(int xval){
		return xval/(GUI.dim.getWidth()-getLength());
	}

	//SETTERS:

	public static void setVisible(boolean b){
		visible = b;
	}
	
	public static void setPages(int p){
		pages = p;
	}

	public static void setX(int x){
		myX = x;
	}

}
