import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;


public class PrintedOCR{

    public static void main(String[] args) {
    	//File Chooser
    	FileSelect j=new FileSelect();
    	BufferedImage orig=null;
    	String selectedfile=j.selectFile();
    	try{
    		orig=ImageIO.read(new File(selectedfile));
    	}catch(IOException e){
    		System.out.println("I/0 Error");return;
    	}
    	BufferedImage bin=binarize(orig);
    	long starttime=System.currentTimeMillis();
    	ArrayList<Block> list=LineSeg(bin);
    	ArrayList<Block> words=new ArrayList<Block>();
    	
    	for(int k=0;k<list.size();k++){CCA Algo=new CCA();
    	ArrayList<Block> Letters=Algo.segmentLetter(bin,list.get(k));
    	
    	Collections.sort(Letters, new CustomComparator());
    	if(k==0)
        	System.out.println("LO"+Letters);
    	Letters=refine(Letters);
    		int sum=0;
    	for(int x=0;x<Letters.size()-1;x++){
    		sum+=Letters.get(x+1).xpos-Letters.get(x).xpos-Letters.get(x).width;
    	}
    	int avg=sum/Letters.size();
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
    	
    	
    	System.out.println(avg+" "+words+" "+spacePosition);
    	for(int qr=0;qr<words.size();qr++){
    		try{
    			ImageIO.write(bin.getSubimage(words.get(qr).xpos, words.get(qr).ypos, words.get(qr).width, words.get(qr).height), "bmp", new File(qr+"word.bmp"));
    		}catch(IOException e){e.printStackTrace();}
    	}
    	try{ImageIO.write(bin.getSubimage(list.get(k).xpos,list.get(k).ypos , list.get(k).width, list.get(k).height), "bmp", new File("dnjsd1.bmp"));}
    	catch(IOException e){
    		
    		e.printStackTrace();
    	}
    	for(int i=0;i<Letters.size();i++)
    	crop(bin,Letters.get(i));
    	Letters.clear();}
    	long endtime=System.currentTimeMillis();
    	System.out.println(endtime-starttime);
    	return;
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
    
    public static ArrayList<Block> LineSeg(BufferedImage orig)
	{
		BufferedImage img					= orig;
		boolean flag						= false;
		int count							= 0;
		int[] startAndEndRowIndex 			= new int[2];
		ArrayList<int[]> listOfIndexPairs 	= new ArrayList<int[]>(); 
		
		int height	= img.getHeight();
		int width	= img.getWidth();

		for(int i=0;i<height;i++) //Y
		{
			
			int j=0;
			for(j=0;j<width;j++) //X
			{
				//checking if it is black then make i as true
				//
				if(img.getRGB(j,i)==0xff000000)
				{
					//add the i value to the hashmap
					if(!flag)
					{
						flag=true;
						startAndEndRowIndex[0] = i;
					}
					
					break;
				}
				if(img.getRGB(j,i)==0xffffffff)
				{
					//do nothing increment j
				}
					
			}//end of j loop
			if(j==width&&flag==true)
			{
				
				startAndEndRowIndex[1] = i-1;System.out.println(startAndEndRowIndex[1] );
				listOfIndexPairs.add(startAndEndRowIndex);
				count++;
				//TODO set flag as false
				flag=false;
				startAndEndRowIndex = new int[2];
			}
			//NOW CALL CROP FOR THE SET OF BLACK PIXEL ROW Which IS A LINE AND THAT HAS TO BE CROPPED
		}//end of i loop
		
		ArrayList<Block> list=new ArrayList<Block>();
		for(int i=0;i<listOfIndexPairs.size();i++)
		{
			//
			startAndEndRowIndex = listOfIndexPairs.get(i);
			int y1=startAndEndRowIndex[0];
			int y2=startAndEndRowIndex[1]-startAndEndRowIndex[0];
			
			list.add(new Block(0, y1, width, y2));;
			
			//find all parameters x,y, w,h
			//parameters are 0,y1,width,y2
		}
		return list;
	}
    
    
    static BufferedImage binarize(BufferedImage orig){
    	int h=orig.getHeight();
    	int w=orig.getWidth();
		
    	BufferedImage bin=orig;
    	
    	for(int i=0;i<h;i++){
    		for(int j=0;j<w;j++){
    			if(bin.getRGB(j,i)>=0xffa00000)
    				bin.setRGB(j, i, 0xffffffff);
    			else
    				bin.setRGB(j, i, 0);
    			
    			
    		}
    		
    	}
    	return bin;
    }
    static int count=0;
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
}

