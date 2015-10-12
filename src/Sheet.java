
//Manages sheets
public class Sheet {

	//Store total pages
	private static int pages = 0;

	//Add grid rows when a new page is created
	public static void paintGrid(){
		
		//Fill up page with grid boxes
		for(int i =(int) (GUI.dim.getWidth()*(pages-1)+1);i<GUI.dim.getWidth()*pages;i+=17){			
			int j = 83;
			int length = (int) (1000000*(1/(1000.0*GUI.tempo.getTempo()/60))/4);
			
			for(int c = 7;c>1;c--){
				GUI.arr.add(new Note(i,j,'b',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'a',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'g',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'f',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'e',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'d',"natural",length,c,1.0));
				j+=17;
				GUI.arr.add(new Note(i,j,'c',"natural",length,c,1.0));
				j+=17;
			}			
		}	

	}
	
	//Increment page number and call paintGrid()
	public static void addPage(){
		pages++;
		paintGrid();
	}
	
	//Return number of pages
	public static int getPages(){
		return pages;
	}
	
	//Assign a value to page number
	public static void setPages(int num){
		pages = num;
		paintGrid();
	}
	
}
