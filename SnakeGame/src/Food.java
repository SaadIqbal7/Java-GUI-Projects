import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class Food implements ImageObserver{
	private int xCoord, yCoord, width, height;
	
	public Food(int xCoord, int yCoord, int foodSize) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		// Taking food size to be of the same height and width
		this.height = foodSize;
		this.width = foodSize;
	}

	public void draw(Graphics g) {
		ImageIcon image = new ImageIcon("apple.png");
		Image apple = image.getImage();
		
		g.drawImage(apple, this.xCoord * width, this.yCoord * this.height, this.width, this.height, this);
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

}
