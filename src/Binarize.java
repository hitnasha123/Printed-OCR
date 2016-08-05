import java.awt.image.BufferedImage;

public class Binarize {
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
}
