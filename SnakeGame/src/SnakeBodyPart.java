import java.awt.Color;
import java.awt.Graphics;

public class SnakeBodyPart {
	private int xCoord, yCoord, width, height;
	
	public SnakeBodyPart(int xCoord, int yCoord, int bodyPartSize) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		// Taking body part to be of the same height and width
		this.height = bodyPartSize;
		this.width = bodyPartSize;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(this.xCoord * width, this.yCoord * this.height, this.width, this.height);
	}
	
	public void drawHead(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(this.xCoord * width, this.yCoord * this.height, this.width, this.height);
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
}
