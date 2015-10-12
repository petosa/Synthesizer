import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;

public class GUI
extends JPanel
implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{

	//Global variables
	
	//Frame size
	public static Dimension dim = new Dimension(816,800);

	//Boolean flags
	public static boolean editmode = true;
	public static boolean silence = false;
	public static boolean shiftPressed;
	public static boolean isBeingScrolled = false;
	public static boolean isBeingRetempoed = false;
	public static boolean isBeingMoved = false;

	//Integers
	public static int currentX = 0;
	public static int notesPlaying = 0;

	//Objects
	public static JFrame frame;
	public static JPanel panel;
	public static Point frozenClick;
	public static Point frozenFrame;

	//All notes on grid are contained in this array
	public static ArrayList<Note> arr = new ArrayList<Note>();

	//Tempo objects
	public static Tempo tempo = new Tempo(60);

	public static void main(String[] args){
		
		GUI t=new GUI();
		
		//Create the frame and set its properties
		frame =new JFrame();
		frame.setUndecorated(true);
		frame.show();
		frame.setResizable(false);
		frame.setSize(dim.width,dim.height);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(t);
		frame.addKeyListener(t);
		frame.addMouseListener(t);
		frame.addMouseMotionListener(t);
		frame.addMouseWheelListener(t);

		//Start tempo timer
		tempo.tick();

		//Initialize Note Array
		arr = new ArrayList();
		
	}
	
	public void paint(Graphics g){

		//Marks clock time at start
		long pre=System.currentTimeMillis();
		//Unknown
		super.paint(g);

		//Detect if form is being moved
		if(isBeingMoved)
			moveJFrame(MouseInfo.getPointerInfo().getLocation());

		//Disable silencer if all notes have been silenced
		if(silence && notesPlaying == 0)
			silence = false;

		//Check if scroll bar is within form
		if(isBeingScrolled && Scrollbar.getX(true) >= 0 && Scrollbar.getX(true) + Scrollbar.getLength() <= dim.getWidth()){
			//Move scroll bar
			Scrollbar.setX(Scrollbar.getOldX() + (-frozenClick.x+MouseInfo.getPointerInfo().getLocation().x));

			//Rectify if scroll bar goes out of bounds
			if(Scrollbar.getX(true) < 0)
				Scrollbar.setX(0);

			if(Scrollbar.getX(true)+Scrollbar.getLength() > dim.getWidth())
				Scrollbar.setX((int) (dim.getWidth()-Scrollbar.getLength()));

			//Scroll all notes
			for(Note n:arr){
				n.setX(n.getOrig()+frame.getX() - ((int) ((Sheet.getPages()-1)*dim.getWidth()*Scrollbar.getCompletion())));				
			}

		}

		//If volume adjustment bar is within bounds
		//and its tempo is being changed
		if(isBeingRetempoed && Tempobar.getX(true) >= 144 && Scrollbar.getX(true) + 19 <= frame.getX() + 650){
			//Move sound bar
			Tempobar.setX(Tempobar.getOldX() + MouseInfo.getPointerInfo().getLocation().x - frozenClick.x);

			//Rectify if scroll bar goes out of bounds
			if(Tempobar.getX(true) < 144){
				Tempobar.setX(144);
			}
			if(Tempobar.getX(true) > 623){
				Tempobar.setX(623);
			}

			//Set tempo based off of X value of slider
			tempo.setTempo(Tempobar.getX(true) - 143);

		}	

		//Draw GUI elements
		drawHUD(g);
		drawTopbar(g);


		//**NOTE READER
		//This segment of code paints notes that are within the view area,
		//plays notes that pass the left bar, and hides notes
		//that pass the left boundary. By doing this last step, we
		//reduce lag

		ArrayList<Note> remove = new ArrayList<Note>();

		for(Note n:arr){
			//Display all notes within viewing area
			if(n.getX(true)<dim.getWidth()){
				n.paint(g);
			}
			//Play notes that pass the left side
			if(n.getX(true) < 33){
				n.play();

				//Updates currentX value, used for reference
				//when pausing a song
				if(!editmode && n.getX(true) != currentX)
					currentX = n.getOrig();
			}
			//Add notes that move off the screen to remove arraylist
			if(n.getX(true) < 15 && !editmode){
				remove.add(n);
			}
		}

		//Hide elements of remove arraylist
		for(Note n:remove){
			n.setVisible(false);
		}

		//Draw remaining GUI elements
		drawBorder(g);	
		drawSidebar(g);
		drawControls(g);

		repaint();

	}

	//Method to draw HUD components and other general components
	public void drawHUD(Graphics g) {

		//Paint Background
		g.setColor(new Color(60,60,60));
		g.fillRect(0,0,1000,1000);//The main body

		//Color HUD bar
		g.setColor(new Color(30,30,30));
		g.fillRect(0,0,1000,82);

		//Draw sponge texture for scroll bar
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/sponge.PNG")), 0, 62, null);

		//Draw scroll bar if there is no song playing
		//and there are at least 2 pages
		if(Scrollbar.getVisible() && editmode)	
			g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/scrubber2.png")), Scrollbar.getX(true), 63,Scrollbar.getLength(),17, null);

		//Colors highlight box over HUD icons based on
		//VisualEffects object. Will only be colored
		//orange if mouse is hovering over it.
		g.setColor(VisualEffects.getColor("boxOffPage"));
		g.drawRect(663,23,42,38);
		g.setColor(VisualEffects.getColor("boxOffRewind"));
		g.drawRect(5,23,42,38);
		g.setColor(VisualEffects.getColor("boxOffPause"));
		g.drawRect(47,23,42,38);
		g.setColor(VisualEffects.getColor("boxOffPlay"));
		g.drawRect(89,23,42,38);
		g.setColor(VisualEffects.getColor("boxOffSave"));
		g.drawRect(707,23,42,38);
		g.setColor(VisualEffects.getColor("boxOffOpen"));
		g.drawRect(751,23,42,38);

		//Draw bars surrounding scroll bar
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0,62,(int) dim.getWidth(), 62);
		g.drawLine(0,80,(int) dim.getWidth(), 80);

		//Draw tempo bar components
		g.setColor(new Color(40,40,40));
		g.fillRect(143,24,500,35);
		g.setColor(new Color(230,230,230,40));
		g.drawRect(143,24,500,35);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/scrubber2.png")), Tempobar.getX(true),25,20,34, null);
		g.setColor(VisualEffects.getColor("sidebar"));
		g.fillRect(144,25,Tempobar.getX(true)-145,34);

	}

	//Draw buttons and title
	public void drawControls(Graphics g) {

		//Draw buttons
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/rewind.png")), 5, 23, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/pause.png")), 47, 23, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/play.png")), 89, 23, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/save.png")), 712, 25, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/open.png")), 752, 24, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/add.png")), 663, 20, null);

		//Draw title
		g.setColor(new Color(155,80,0,255));
		g.setFont(new Font("Courier",0,17));
		g.drawString("Synthesizer - by Nick Petosa!",250,17);

	}

	//Draw notes on left sidebar
	public void drawSidebar(Graphics g) {

		//Draw left sidebar
		g.setColor(new Color(0,0,0,1f));
		g.fillRect(2,82,32,715);	
		g.setColor(VisualEffects.getColor("sidebar"));	

		//Draw letters on sidebar
		int sum = -13;
		for(int x = 7;x >= 2;x--){
			g.drawString("b"+x,6,110+sum);sum+=20;	
			g.drawString("a"+x,6,107+sum);sum+=13;
			g.drawString("g"+x,6,110+sum);sum+=19;
			g.drawString("f"+x,6,110+sum);sum+=16;
			g.drawString("e"+x,6,110+sum);sum+=17;
			g.drawString("d"+x,6,110+sum);sum+=17;
			g.drawString("c"+x,6,110+sum);sum+=17;
		}

	}

	//Draw top bar and components
	public void drawTopbar(Graphics g){

		//Top scrubber
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/scrubber.png")), -1, 0, null);

		//Draw text on top bar
		g.setColor(new Color(248,248,255));
		g.setFont(new Font("Courier",0,19));
		g.drawString("x",(int) (dim.getWidth()-29),16);
		g.drawString("_",(int) (dim.getWidth()-69),10);

		//Color top bar minimize/exit buttons
		g.setColor(VisualEffects.getColor("exitButton"));
		g.fillRect((int) (dim.getWidth()-43),3,40,17);
		g.setColor(VisualEffects.getColor("minimizeButton"));
		g.fillRect((int) (dim.getWidth()-83),3,40,17);
	}

	//Draw side borders and component borders
	public void drawBorder(Graphics g){

		//Draw horizontal bars
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/bottomscrubber.png")), -1, (int) (dim.getWidth()-3), null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/bottomscrubber.png")), -1, 20, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/bottomscrubber.png")), -1, 797, null);

		//Draw vertical bars
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/sidescrubber.png")), 0, -1, null);
		g.drawImage(Toolkit.getDefaultToolkit().getImage(Synth.class.getResource("res/sidescrubber.png")), (int) (dim.getWidth()-3), 0, null);

	}

	//Key press listener
	public void keyPressed (KeyEvent e) {
		//Shift pressed flag used for note grouping
		if(e.getKeyCode() == KeyEvent.VK_SHIFT){
			shiftPressed = true;
		}	
	}

	//Move frame location to specified point
	public static void moveJFrame(Point p){
		frame.setLocation(frozenFrame.x + p.x - frozenClick.x ,frozenFrame.y + p.y - frozenClick.y);
	}

	//Mouse click listener
	@Override
	public void mouseClicked(MouseEvent e){

		//If exit button is clicked, exit frame
		if(Utils.isIn("exitButton", MouseInfo.getPointerInfo().getLocation(), frame.getLocation()))
			System.exit(1);

		//If minimize button is clicked, minimize frame
		if(Utils.isIn("minimizeButton", MouseInfo.getPointerInfo().getLocation(), frame.getLocation())) {
			frame.setState(Frame.ICONIFIED);		
		}

	}

	//Mouse release listener
	@Override
	public void mouseReleased(MouseEvent e) {
		//Reset flags
		isBeingMoved = false;
		isBeingScrolled = false;
		isBeingRetempoed = false;
	}

	//Mouse press listener
	@Override
	public void mousePressed(MouseEvent e) {

		//Store old values in objects
		Scrollbar.storeX();
		Tempobar.storeX();

		//Store mouse point
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();

		//Store point and frame location in case user is moving frame
		frozenClick = mousePoint;
		frozenFrame = frame.getLocation();

		//Make isBeingMoved true if mouse is in correct area
		if(Utils.isIn("titleBar", mousePoint, frame.getLocation()))
			isBeingMoved = true;				

		//Try to get note at point
		Note nu = Utils.findBox(mousePoint);

		//If this is a note and we can edit...
		if(nu != null && editmode){

			//Left Click
			if (e.getButton() == MouseEvent.BUTTON1 && mousePoint.x-frame.getX() > 35){

				//Case: Create sustained note
				if(shiftPressed){				
					nu.sustain.convertAdjacent(0);
					try{
						nu.test();
					}
					catch(Exception e2){}					
				}
				else if(!nu.getExists()){
					nu.setExists(true);
					nu.test();
				}

				//Case: Change note accidental
				else if(!nu.sustain.getSustain()){

					if(nu.getAccidental().equals("flat"))
						nu.setAccidental("natural");

					else if(nu.getAccidental().equals("natural"))
						nu.setAccidental("sharp");

					else if(nu.getAccidental().equals("sharp"))
						nu.setAccidental("flat");

					nu.test();
				}

				//Case: Change sustained accidental
				else if(nu.sustain.getSustain()){
					nu.sustain.changeSustainAccidental(nu);
					nu.test();
				}

			}

			//Right Click
			if (e.getButton() == MouseEvent.BUTTON3 && nu.getExists()){

				//Case: Break sustain
				if(shiftPressed)
					nu.sustain.sustainBreak(nu);

				//Case: Delete note/sustained portion
				else{
					nu.sustain.sustainDelete(nu);
					nu.setColor(VisualEffects.getColor("grid"));
					nu.setExists(false);   
					nu.sustain.clearData();
					nu.setAccidental("natural");
					nu.setVolume(1.0f);
					nu.setPlayed(false);

					//Update highlight
					VisualEffects.reactToHover(mousePoint,frame.getLocation());

				}
			}

		}

		//Button checkers
		else if (e.getButton() == MouseEvent.BUTTON1){

			//Case: Add page
			if(editmode && Utils.isIn("addPage", mousePoint, frame.getLocation()))
			{
				if(Scrollbar.getPages() > 0)
					Scrollbar.setVisible(true);

				Sheet.addPage();
				Scrollbar.setPages(Sheet.getPages());
			}

			//Case: Save notes
			if(editmode && Utils.isIn("save", mousePoint, frame.getLocation()))
			{
				Utils.adjustLength();
				Save.start();
			}

			//Case: Open notes
			if(editmode && Utils.isIn("open", mousePoint, frame.getLocation()))		
				Open.start();

			//Case: Play notes
			if(editmode && Utils.isIn("play", mousePoint, frame.getLocation()) && Sheet.getPages() > 0)
			{

				Utils.adjustLength();

				for(Note n:arr)
					if(n.getX(true) < 33)
						n.setPlayed(true);

				editmode = false;

			}

			//Case: Pause notes
			if(!editmode && Utils.isIn("pause", mousePoint, frame.getLocation()))
				Utils.pause();

			//Case: Rewind notes
			if(Utils.isIn("rewind", mousePoint, frame.getLocation()))
			{
				Utils.rebuildSheet();
				Scrollbar.setX(0);
				Utils.sync(0);
				Utils.stopSounds();
				currentX = 0;
			}

			//Case: Clicked scrollbar
			else if(Utils.isIn("scrollbar", mousePoint, frame.getLocation()) && editmode)
				isBeingScrolled = true;

			//Case: Clicked tempobar
			if(editmode && Utils.isIn("tempobar", mousePoint, frame.getLocation()))			
				isBeingRetempoed = true;

		}
	}

	//Mouse move listener
	@Override
	public void mouseMoved(MouseEvent e) {
		VisualEffects.reactToHover(MouseInfo.getPointerInfo().getLocation(),frame.getLocation());
	}

	//Key release listener
	@Override
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			shiftPressed = false;
	}

	//Mouse wheel listener (Adjusts note volume)
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		
		
		//Case: Mouse wheel scrolled up
		if(e.getWheelRotation() < 0 && Utils.isIn("box", mousePoint,frame.getLocation())){		
			Note nu = Utils.findBox(mousePoint);
			
			//If this is a normal note
			if(nu.getVolume() < 1 && nu.getExists() && !nu.sustain.getSustain())
				nu.setVolume(nu.getVolume()+0.1);
			
			//If this is a sustained note
			else if(nu.getVolume() < 1 && nu.getExists() && nu.sustain.getSustain())
				nu.sustain.setSustainVolume(nu,nu.getVolume()+0.1);
		}
		
		//Case: Mouse wheel scrolled down
		else if(e.getWheelRotation() > 0 && Utils.isIn("box", mousePoint,frame.getLocation())){				
			Note nu = Utils.findBox(mousePoint);

			//If this is a normal note
			if(nu.getVolume() > .2 && nu.getExists() && !nu.sustain.getSustain())
				nu.setVolume(nu.getVolume()-0.1);
			
			//If this is a sustained note
			else if(nu.getVolume() > .2 && nu.getExists() && nu.sustain.getSustain())
				nu.sustain.setSustainVolume(nu,nu.getVolume()-0.1);
		}

	}

	//Unused methods of the implemented classes
	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}

	@Override
	public void mouseDragged(MouseEvent e) {}

}