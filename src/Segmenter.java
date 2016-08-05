import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;



public class Segmenter implements Runnable {
	private String threadName;
	private Thread t;
	private Block lineSegment;
	private BufferedImage bin;
	private static int count;
	public Segmenter(String thread,Block block,BufferedImage bin){
		this.threadName=thread;
		lineSegment=block;
		this.bin=bin;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		CCA Algo=new CCA("Processing",lineSegment,bin);
		ArrayList<Block> Letters=Algo.segmentLetter();
	System.out.println(Letters);
	Collections.sort(Letters, new CustomComparator());
	Letters=refine(Letters);
	int avg=calculateAvgLSize(Letters);
	ArrayList<Block> words=findSpaces(Letters, avg);
	for(int i=0;i<Letters.size();i++)
		crop(bin,Letters.get(i));
	}
	
	
	private static ArrayList<Block> findSpaces(ArrayList<Block> Letters,int avg){
		ArrayList<Block> words=new ArrayList<Block>();
		Block temp=new Block();
    	temp.clone(Letters.get(0));
    	int ind=0;
    	ArrayList<Integer> spacePosition=new ArrayList<Integer>();
    	spacePosition.add(0);
    	int spaceCount=0;
    	for(int y=0;y<Letters.size()-1;y++){
    		boolean condition=(Letters.get(y+1).xpos-Letters.get(y).xpos-Letters.get(y).width)<=2*avg;
    		
    		if(condition){
    			spacePosition.set(spaceCount, spacePosition.get(spaceCount)+1);
    			temp.width=Letters.get(y+1).xpos+Letters.get(y+1).width-Letters.get(ind).xpos;
    			temp.ypos=(temp.ypos<Letters.get(y+1).ypos)?temp.ypos:Letters.get(y+1).ypos;
    			temp.height=(temp.height>Letters.get(y+1).height)?temp.height:Letters.get(y+1).height;
    		}
    		else{
    			spacePosition.add(spacePosition.get(spaceCount)+1);
    			spaceCount++;
    			words.add(temp);
    			temp=new Block();
    			temp.clone(Letters.get(y+1));
    			ind=y+1;
    		}
    	}
    	words.add(temp);
		return words;
	}
	
	static void crop(BufferedImage original,Block block){
    	System.out.println(block);
    	BufferedImage crop=original.getSubimage(block.xpos, block.ypos, block.width, block.height);
    	count++;
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyyhmmssa");
    	String formattedDate = sdf.format(date);
    	Random rand=new Random();
    	
    	File output=new File(count+"_crop"+formattedDate+rand.nextInt(100000)+".bmp");System.out.println("_crop"+formattedDate+rand.nextInt(100000)+".bmp");
    	try{
    		ImageIO.write(crop, "bmp", output);
    	}catch(IOException e){
    		System.out.print("I/O Error"+output.getName());
    		
    	}
		return;
    }
	
	
	private static int calculateAvgLSize(ArrayList<Block> Letters){
		int sum=0;
		for(int x=0;x<Letters.size()-1;x++){
    		sum+=Letters.get(x+1).xpos-Letters.get(x).xpos-Letters.get(x).width;
    	}
		return sum/Letters.size();
	}
	
	
	private static ArrayList<Block> refine(ArrayList<Block> letters) {
		// TODO Auto-generated method stub
    	for(int i=0;i<letters.size()-1;i++){
    		boolean condition=letters.get(i).xpos+letters.get(i).width>=letters.get(i+1).xpos+letters.get(i+1).width;
    		if(condition){
    			letters.get(i).ypos=(letters.get(i).ypos>letters.get(i+1).ypos)?letters.get(i+1).ypos:letters.get(i).ypos;
    			letters.get(i).height=(letters.get(i).height>letters.get(i+1).height)?letters.get(i).height:letters.get(i+1).height;
    			letters.remove(i+1);
    		}
    		
    	}
		return letters;
	}
	
	
	public static class CustomComparator implements Comparator<Block> {
        
		@Override
		public int compare(Block arg0, Block arg1) {
			// TODO Auto-generated method stub
			
			return Integer.compare(arg0.xpos, arg1.xpos);
		}
    }
	
	
	public void start ()
	   {
		
	      System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	         
	      }
	      
	   }

}
