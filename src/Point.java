
public class Point {
	int x;
	int y;
	
	Point(int x,int y){
		this.x=x;
		this.y=y;
	}
	public String toString(){
		return String.format("X:%d Y:%d", x,y);
	}
}
