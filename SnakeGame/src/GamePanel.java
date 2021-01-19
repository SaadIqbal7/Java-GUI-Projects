import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

// Class in which the game resides
public class GamePanel extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	
	private static final int width = 800;
	private static final int height = 800;
	private static final int offset = 200;
	
	private Thread thread;
	private boolean isRunning;
	
	private SnakeBodyPart snakeBodyPart;
	private ArrayList<SnakeBodyPart> snake;
	private final int elementSize = 20;
	
	private int xCoord = 10, yCoord = 10, size = 5;
	private int iterations = 0;
	
	private boolean right = true, left = false, up = false, down = false;
	
	
	private Food food = null;
	private Random rand = new Random();
	
	private int scoreSize = 10;
	private int score = 0;
	
	public GamePanel() {
		setFocusable(true);
		
		setPreferredSize(new Dimension(width + offset, height));

		addKeyListener(this);
		
		snake = new ArrayList<SnakeBodyPart>();
		
		start();
	}
	
	public void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		isRunning = false;
		
		try {
			thread.join();
		} catch(InterruptedException e) {
			System.out.println("Thread execution tnterrupted by some other thread");
		}
	}
	
	private void gameLoop() {
		if(snake.size() == 0) {
			snakeBodyPart = new SnakeBodyPart(this.xCoord, this.yCoord, this.elementSize);
			snake.add(snakeBodyPart);
		}
		
		this.iterations++;
		// 250000 Indicates the speed of snake, greater the number slower the snake
		if(this.iterations > 400000) {
			if(this.right) {
				this.xCoord++;
			} else if(this.left) {
				this.xCoord--;
			} else if(this.down) {
				this.yCoord++;
			} else if(this.up) {
				this.yCoord--;
			}
			
			this.iterations = 0;
			
			snakeBodyPart = new SnakeBodyPart(this.xCoord, this.yCoord, this.elementSize);
			snake.add(snakeBodyPart);
			
			if(snake.size() > this.size) {
				this.snake.remove(0);
			}
		}
		
		if(this.food == null) {
			int x = rand.nextInt((GamePanel.width / this.elementSize) - 1);
			int y = rand.nextInt((GamePanel.height / this.elementSize) - 1);
			
			this.food = new Food(x, y, this.elementSize);
		}
		
		if(this.xCoord == food.getxCoord() && this.yCoord == food.getyCoord()) {
			this.food = null;
			this.size++;
			this.score++;
		}
		
		
		if(this.xCoord < 0 || this.xCoord > ((GamePanel.width / this.elementSize) - 2) || this.yCoord < 0 || this.yCoord > ((GamePanel.height / this.elementSize) - 2)) {
			stop();
		}
		
		for(int i = 0; i < snake.size() - 1; i++) {
			if(this.xCoord == snake.get(i).getxCoord() && this.yCoord == snake.get(i).getyCoord()) {
				stop();
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, width + offset, height);
		
		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, width + offset, height);
		g.fillRect(0, 0, width + offset, height);
		
		for(int i = 0; i < (width + offset)/10; i++) {
			g.drawLine(i * 10, 0, i * 10, height);
		}
		
		for(int i = 0; i < height/10; i++) {
			g.drawLine(width, i * 10, 0, i * 10);
		}
		
		for(int i = 0; i < snake.size(); i++) {
			if(i == snake.size() - 1) {
				snake.get(i).drawHead(g);
			} else {
				snake.get(i).draw(g);
			}
		}
		
		if(this.food != null) {
			this.food.draw(g);
		}
		
		g.setColor(Color.green);
		g.fillRect(width, 0, 2, height);
		
		// Convert score to string
		String strScore = Integer.toString(this.score);
		for(int i = 0; i < strScore.length(); i++) {
			drawDigit(g, strScore.charAt(i), i, strScore.length());
		}
	}
	
	// Methods that runs in the thread when thread's start() method is called
	@Override
	public void run() {
		while(isRunning) {
			gameLoop();
			repaint();
		}
	}
	
	
	// Key listener methods
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Ensuring left is not enabled
		if(key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			down = false;
			up = false;
		} else if(key == KeyEvent.VK_LEFT && !right) {
			down = false;
			up = false;
			left = true;
		} else if(key == KeyEvent.VK_DOWN && !up) {
			right = false;
			down = true;
			left = false;
		} else if(key == KeyEvent.VK_UP && !down) {
			right = false;
			up = true;
			left = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public void drawDigit(Graphics g, char digit, int position, int numberOfDigits) {
		
		int digitWidth = 50;
		
		int offset;
		if(numberOfDigits == 1) {
			// score 1 digit
			offset = 80;
		} else if(numberOfDigits == 2) {
			// score 2 digits
			offset = 50;
		} else if(numberOfDigits == 3) {
			// score 3 digits
			offset = 30;
		} else {
			// score 4 digits
			offset = 5;
		}
		
		g.setColor(Color.GREEN);
		if(digit == '0') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '1') {
			// Right
			for(int i = -2; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '2') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 1; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 1; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = 1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '3') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '4') {
			// Right
			for(int i = -2; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -2; i < 2; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '5') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = 1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 0; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -1; i < 1; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '6') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = 1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 0; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '7') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '8') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 0; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		} else if(digit == '9') {
			// Top
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - 2 * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Right
			for(int i = -1; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 3 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Middle
			for(int i = 0; i < 3; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-1) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Left
			for(int i = -1; i < 1; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + 0 * this.scoreSize, GamePanel.height / 2 + i * this.scoreSize, this.scoreSize, this.scoreSize);
			}
			// Bottom
			for(int i = 0; i < 4; i++) {
				g.fillRect(position * digitWidth + GamePanel.width + offset + i * this.scoreSize, GamePanel.height / 2 - (-3) * this.scoreSize, this.scoreSize, this.scoreSize);
			}
		}
	}
}
