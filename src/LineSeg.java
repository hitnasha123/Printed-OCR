import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class LineSeg 
{
	public LineSeg(BufferedImage orig)
	{
		BufferedImage img					= orig;
		boolean flag						= false;
		int count							= 0;
		int[] startAndEndRowIndex 			= new int[2];
		ArrayList<int[]> listOfIndexPairs 	= new ArrayList<int[]>(); 
		ArrayList<BufferedImage> Lines		= new ArrayList<BufferedImage>();

		
		
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
				
				startAndEndRowIndex[1] = i-1;//System.out.println(startAndEndRowIndex[1] );
				listOfIndexPairs.add(startAndEndRowIndex);
				count++;
				System.out.println(listOfIndexPairs.get(0)[1]);
				//TODO set flag as false
				flag=false;
				startAndEndRowIndex = new int[2];
			}
			//NOW CALL CROP FOR THE SET OF BLACK PIXEL ROW Which IS A LINE AND THAT HAS TO BE CROPPED
		}//end of i loop
		
		//System.out.println(listOfIndexPairs.get(0)[1]);
		
		for(int i=0;i<listOfIndexPairs.size();i++)
		{
			//
			startAndEndRowIndex = listOfIndexPairs.get(i);
			int y1=startAndEndRowIndex[0];
			int y2=startAndEndRowIndex[1]-startAndEndRowIndex[0];
			Lines.add(crop(img,0,y1,width,y2));
			
			//find all parameters x,y, w,h
			//parameters are 0,y1,width,y2
		}
		
	}
			 static BufferedImage crop(BufferedImage img,int x,int y1,int width,int y2)
			 {
			    	System.out.println(x+" "+y1+" "+width+" "+y2);
			    	BufferedImage crop=img.getSubimage(x,y1,width,y2);
			    	
			    	return crop;
					
			    }
			
			//call crop
			
			//write to file system
			
		
	
	//
}