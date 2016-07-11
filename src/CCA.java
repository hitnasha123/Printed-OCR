import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.imageio.ImageIO;

public class CCA {
	 ArrayList<ArrayList<Integer>> label;
	 //Stores all of the labels by pixel
	 
	 ArrayList<Set<Integer>> equivalence;
	 //Stores all of the equivalence classes
	 
	 ArrayList<Block> segmentLetter(BufferedImage orig,Block block){
		 BufferedImage bin=orig;
	    	File output=new File("_crop"+".bmp");
	    	try{
	    		ImageIO.write(bin, "bmp", output);
	    	}catch(IOException e){
	    		System.out.print("I/O Error");
	    		
	    	}
		ArrayList<Block> letters=new ArrayList<Block>();
		/*This is where the final letter blocks are stored, which contains the following info:
		 * 1.X-coord
		 * 2.Y-Coord
		 * 3.Height
		 * 4.Width
		 * Which are essential for crop function*/
		Block temp1=null;
		Vector<Vector<Point>> group=new Vector<Vector<Point>>();
		/*Creates The Array of points associated with */
		equivalence=new ArrayList<Set<Integer>>();
		/*Here We have a Set of Labels which are equivalent to a particular label, set is useful here since No repeated elements are automatically rejected*/
		int check=0;
		int h=block.height;int w=block.width;
		int labelcount=0;
		/*This variable is used to increment the labelcount whenever an isolated pixel is reached*/
		Integer[] b=new Integer[w];
		label=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<h;i++){
			label.add(i, new ArrayList<Integer>());
			for(int j=0;j<w;j++){
				/*Initialising Label of every pixel to 0 in the beginning*/
				label.get(i).add(0);
			}
		}
		
		
		
		for(int i=0;i<h;i++){
			
			for(int j=0;j<w;j++){
				/*First Pass scans through every vertex*/
				if(orig.getRGB(block.xpos+j,block.ypos+i )==0xff000000){
					
					//If The vertex is black check for neighbours
				check=checkNeighbours(i,j);
				if(check!=0)
				{
					//If neighbour exists assign the minimum valued neighbour as label for current pixel
					label.get(i).set(j, check);
					
					/*Adds the point to the group of points associated with the label*/
					group.get(check-1).add(new Point(j,i));
					
				}
				
				else
					{//If no neighbour is obtained
					label.get(i).set(j,++labelcount);
					//Assign pixel the incremented label count
					Set<Integer> temp = new TreeSet<Integer>();
					equivalence.add(temp);
					//Add an extra space for the next label in equivalance classes
					Vector<Point> temp4 = new Vector<Point>();
					
					group.add(temp4);
					//Adds an extra space for the Group of pixels associated with Label
					try{group.get(labelcount-1).add(new Point(j,i));}catch(ArrayIndexOutOfBoundsException e){
						
						e.printStackTrace();
					}
					//Adds The point to the Label group
					}
				
				}
				else
					{}
			}
			
		}
		for(int i=group.size()-1;i>=0;i--){
			if(equivalence.get(i).iterator().hasNext()){
				if(equivalence.get(i).iterator().next()<(i+1)){
					
					/*If there is a lower valued equivalance class merge(Union) the points associated*/
					group.get(equivalence.get(i).iterator().next()-1).addAll(group.get(i));
					group.remove(i);
					
					
				}
			}
		}
		for(int i=0;i<group.size();i++){
			/*Finding the minimum and maximum X and Y coordinate for crop parameters*/
			temp1=new Block();
			int minx=Integer.MAX_VALUE;
			int miny=Integer.MAX_VALUE;
			int maxx=block.xpos+group.get(i).get(0).x;
			int maxy=block.ypos+group.get(i).get(0).y;
			for(int j=0;j<group.get(i).size();j++){
				if(block.xpos+group.get(i).get(j).x<minx){
					minx=block.xpos+group.get(i).get(j).x;
				}
				if(block.xpos+group.get(i).get(j).x>maxx){
					maxx=block.xpos+group.get(i).get(j).x;
				}if(block.ypos+group.get(i).get(j).y<miny){
					miny=block.ypos+group.get(i).get(j).y;
				}if(block.ypos+group.get(i).get(j).y>maxy){
					maxy=block.ypos+group.get(i).get(j).y;
				}
			}
			temp1.xpos=minx;
			temp1.ypos=miny;
			temp1.height=maxy-miny+2;
			temp1.width=maxx-minx+1;
			letters.add(temp1);
		}
		
		
		return letters;
	}
	 public static int convert(int n) {
		  return Integer.valueOf(String.valueOf(n), 16);
		}

	private int checkNeighbours(int i, int j) {
		// TODO Auto-generated method stub
		//Checks Neighbours
		int neighbour=0;
		//Neighbour array N,NE,E,SE,S...... respectively
		int[] d=new int[4];
		//Gets the label depending upon the position of the pixel
		try{
		if(i==0&&j==0){
			
		}
		else if(i==0)
		{
			
			d[2]=label.get(i).get(j-1);
		}
		else if(j==0){
			d[0]=label.get(i-1).get(j);
			d[1]=label.get(i-1).get(j+1);
			
		}
		
		else{
			d[0]=label.get(i-1).get(j);
			d[1]=label.get(i-1).get(j+1);
			d[2]=label.get(i).get(j-1);
			d[3]=label.get(i-1).get(j-1);
			
		}}catch(ArrayIndexOutOfBoundsException e){
			
		}neighbour=Integer.MAX_VALUE;
		for (int i1=0;i1<4;i1++){
			
			
			if(d[i1]!=0&&(d[i1]<neighbour)){
				//Checking for the least value labelled neighbour
				neighbour=d[i1];
			}
		}
		for (int i1=0;i1<4;i1++){
			if(d[i1]!=0){
				//Adds all of the valued neighbour labels to equivalence class of the current pixels label
				equivalence.get(d[i1]-1).add(neighbour);
			}
		}
		if(neighbour==Integer.MAX_VALUE)return 0;
		return neighbour;
	}
	
	

}