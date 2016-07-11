
public class Block {
	int xpos;
	int ypos;
	int width;
	int height;
	public Block(){}
	
	public Block(int x,int y,int width,int height){
		xpos=x;
		ypos=y;
		this.width=width;
		this.height=height;
	}
	public String toString(){
		return String.format("Xpos:%d Ypos:%d Width:%d Height:%d", xpos,ypos,width,height);
	}
	public void clone(Block block) {
		// TODO Auto-generated method stub
		xpos=block.xpos;
		ypos=block.ypos;
		height=block.height;
		width=block.width;
		
	}
}
