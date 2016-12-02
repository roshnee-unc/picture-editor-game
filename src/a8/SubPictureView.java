package a8;

@SuppressWarnings(value = {"serial" })
public class SubPictureView extends PictureView {
	private int x;
	private int y;
	
	public SubPictureView(int x, int y, ObservablePicture p) {
		super(p);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void decreaseXByOne() {
		x--;
	}
	
	public void decreaseYByOne() {
		y--;
	}
	
	public void increaseXByOne() {
		x++;
	}
	
	public void increaseYByOne() {
		y++;
	}
	
}
