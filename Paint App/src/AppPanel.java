import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AppPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	
	// Width and height of the app
	private static final int appWidth = 1200;
	private static final int appHeight = 900;
	
	private static final Color[] colors = {Color.BLACK,
			Color.WHITE,
			Color.RED,
			Color.PINK,
			Color.GRAY,
			Color.YELLOW,
			Color.GREEN,
			Color.BLUE,
			Color.ORANGE,
			Color.CYAN,
			Color.DARK_GRAY,
			Color.LIGHT_GRAY};
	
	private Color currentColor = colors[0];
	private int colorHeight = 96;
	private int colorWidth = 175;
	
	private boolean dragging = false;
	private boolean clear = false;
	
	private ArrayList<Shape> drawings = new ArrayList<Shape>();
	private Shape drawing;
	private ShapeType selectedShapeType = ShapeType.LINE;

	final Float[] brushSizes = {2f, 3f, 5f, 7f, 10f, 15f, 20f, 30f, 50f};
	JComboBox<Float> brushSizeMenu = new JComboBox<Float>(brushSizes);
	
	public AppPanel() {
		setFocusable(true);
		setPreferredSize(new Dimension(appWidth, appHeight));
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// This will allow use to keep element where ever we want
		this.setLayout(null);
		
		JLabel label = new JLabel();
		label.setText("Brush Size");
		
		label.setBounds(AppPanel.appWidth - 200 + 50, 15, 100, 50);
		brushSizeMenu.setBounds(AppPanel.appWidth - 200 + 50, 50, 100, 50);
		this.add(brushSizeMenu);
		this.add(label);
	}
	
	@Override	
    public void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
    	
    	// Make drawings
    	drawOnScreen(g2d);
    	
    	g2d.setColor(Color.LIGHT_GRAY);
    	// Draw vertical panel
    	g2d.fillRect(AppPanel.appWidth - 200, 0, 5, AppPanel.appHeight - 200);

    	// Draw shapes buttons
    	g2d.fillRect(AppPanel.appWidth - 200 + 50, 150, 100, 100);
    	g2d.fillRect(AppPanel.appWidth - 200 + 50, 275, 100, 100);
    	g2d.fillRect(AppPanel.appWidth - 200 + 50, 400, 100, 100);
    	
    	// Draw action buttons
    	g2d.fillRect(AppPanel.appWidth - 200 + 50, 525, 100, 50);
    	g2d.fillRect(AppPanel.appWidth - 200 + 50, 600, 100, 50);
    	g2d.setColor(Color.BLACK);
    	
        g2d.setStroke(new BasicStroke(3f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        
    	// Draw shapes
    	g2d.drawLine(AppPanel.appWidth - 200 + 70, 230, AppPanel.appWidth - 200 + 130, 170);
    	g2d.drawRect(AppPanel.appWidth - 200 + 60, 295, 80, 60);
    	g2d.drawOval(AppPanel.appWidth - 200 + 60, 420, 80, 60);
    	
    	// Add text to action button
    	g2d.drawString("UNDO", AppPanel.appWidth - 200 + 80, 555);
    	g2d.drawString("ERASE", AppPanel.appWidth - 200 + 80, 630);
    	
    	// Draw color panel at the bottom
    	// Draw a border line
    	g.fillRect(0, AppPanel.appHeight - 200, AppPanel.appWidth, 5);
    	
    	for(int i = 0; i < 2; i++) {
    		for(int j = 0; j < 5; j++) {
    			g2d.setColor(Color.BLACK);
    			// Gray a gray border around every color
    			// Top border
    			g2d.fillRect(j * this.colorWidth, AppPanel.appHeight - 200  + 5 + i * this.colorHeight, 2 + this.colorWidth, 2);
    			// Left side border
    			g2d.fillRect(j * this.colorWidth, AppPanel.appHeight - 200  + 5 + i * this.colorHeight, 2, this.colorHeight + 2);
    			// Right side border
    			g2d.fillRect(j * this.colorWidth + this.colorWidth + 2, AppPanel.appHeight - 200  + 5 + i * this.colorHeight, 2, this.colorHeight + 2);
    			// Bottom border
    			g2d.fillRect(j * this.colorWidth, AppPanel.appHeight - 200  + 5 + i * this.colorHeight + this.colorHeight + 2, 2 + this.colorWidth, 2);
    			
    			// Add color
    			g2d.setColor(colors[i * 5 + j]);
    			g2d.fillRect(j * this.colorWidth + 2, AppPanel.appHeight - 200  + 5 + i * this.colorHeight + 2,this.colorWidth, this.colorHeight);
    		}
    	}
    	
    	g2d.setColor(Color.GRAY);
    	// Add clear button
    	g2d.fillRect((this.colorWidth * 5 + 2 * 5), AppPanel.appHeight - 200 + 5, AppPanel.appWidth - (this.colorWidth * 5 + 2 * 10), 200 - 2);
    	g2d.setColor(Color.WHITE);
    	g2d.drawString("CLEAR SCREEN", (this.colorWidth * 5 + 2 * 5) + 120, AppPanel.appHeight - 200 + 5 + 100);
    }
	
    private void drawOnScreen(Graphics2D g2d) {
    	if(clear == true) {
    		drawings.clear();
    		clear = false;
    	}

    	// Draw curve by drawing small lines from previous position to current position
    	for(int i = 0; i < drawings.size(); i++) {
			Shape shape = drawings.get(i);
	    	g2d.setColor(shape.color);
	        g2d.setStroke(new BasicStroke(shape.thickness,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
	        
    		if(shape.shapeType == ShapeType.LINE) {

    	        for(int j = 0; j < shape.coordinates.size() - 1; j++) {
            		g2d.drawLine(
            				shape.coordinates.get(j).x, 
            				shape.coordinates.get(j).y, 
            				shape.coordinates.get(j + 1).x, 
            				shape.coordinates.get(j + 1).y);
            	}
    		} else if(shape.shapeType == ShapeType.RECTANGLE) {
    			int x1 = shape.coordinates.get(0).x;
    			int x2 = shape.coordinates.get(shape.coordinates.size() - 1).x;
    			int y1 = shape.coordinates.get(0).y;
    			int y2 = shape.coordinates.get(shape.coordinates.size() - 1).y;
    			
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                
    		} else if(shape.shapeType == ShapeType.CIRCLE) {
    			int x1 = shape.coordinates.get(0).x;
    			int x2 = shape.coordinates.get(shape.coordinates.size() - 1).x;
    			int y1 = shape.coordinates.get(0).y;
    			int y2 = shape.coordinates.get(shape.coordinates.size() - 1).y;
    			
                g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    		}
    	}
    }
    
    private void selectColor(int x, int y) {
    	// Check if color is clicked
    	if(x > (this.colorWidth * 5 + 2 * 5) || y < AppPanel.appHeight - 200 + 5) {
    		return;
    	}
    	
    	int xCoord = x / (colorWidth + 4);
    	int yCoord = y / (AppPanel.appHeight - 200 + this.colorHeight + 4);
    	
    	currentColor = colors[yCoord * 5 + xCoord];
    }
    
    private void selectShape(int x, int y) {
    	// Check that mouse click is in vertical menu
    	if(x < (AppPanel.appWidth - 200) || y > AppPanel.appHeight - 200) {
    		return;
    	}

    	if((x > AppPanel.appWidth - 200 + 50) && (x < AppPanel.appWidth - 200 + 50 + 100) && (y > 150) && (y < 250)) {
    		this.selectedShapeType = ShapeType.LINE;
    	} else if((x > AppPanel.appWidth - 200 + 50) && (x < AppPanel.appWidth - 200 + 50 + 100) && (y > 275) && (y < 375)) {
    		this.selectedShapeType = ShapeType.RECTANGLE;
    	} else if((x > AppPanel.appWidth - 200 + 50) && (x < AppPanel.appWidth - 200 + 50 + 100) && (y > 400) && (y < 500)) {
    		this.selectedShapeType = ShapeType.CIRCLE;
    	}
    }
    
    private void takeAction(int x, int y) {
    	// Check that mouse click is in vertical menu
    	if(x < (AppPanel.appWidth - 200) || y > AppPanel.appHeight - 200) {
    		return;
    	}

    	if((x > AppPanel.appWidth - 200 + 50) && (x < AppPanel.appWidth - 200 + 50 + 100) && (y > 525) && (y < 575)) {
    		if(this.drawings.size() > 0) {
        		this.drawings.remove(this.drawings.size() - 1);
        		repaint();
    		}
    	} else if((x > AppPanel.appWidth - 200 + 50) && (x < AppPanel.appWidth - 200 + 50 + 100) && (y > 600) && (y < 650)) {
    		// Select white color
    		this.currentColor = Color.WHITE;
    		// Add line shape
        	this.drawing = new Shape(ShapeType.LINE, this.currentColor, (Float) brushSizeMenu.getSelectedItem());
        	this.drawings.add(drawing);
    	}
    }

    private void setupShape(int x, int y) {
    	// Check if mouse is on the white board
		if(y > AppPanel.appHeight - 200 + 5 || x > AppPanel.appWidth - 200) {
			return;
		}

    	this.drawing = new Shape(this.selectedShapeType, this.currentColor, (Float) brushSizeMenu.getSelectedItem());
    	this.drawing.coordinates.add(new Coordinates(x, y));
    	
    	this.drawings.add(drawing);
    	
    	// Set dragging = true
    	this.dragging = true;
    }
    
    private void clearScreen(int x, int y) {
    	// Check if clear screen button is clicked
		if(x > (this.colorWidth * 5 + 2 * 5) && y > AppPanel.appHeight - 200 + 5) {
			clear = true;
			// Clear the white board
			repaint();
		}
		return;
    }
    
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getX();
		int y = e.getY();
		
		if(dragging == true) {
			return;
		}
		
		clearScreen(x, y);
		selectColor(x, y);
		selectShape(x, y);
		takeAction(x, y);
		setupShape(x, y);
	}

	// Gets called when user is drawing and releases the button
	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.dragging == false) {
			return;
		}
		this.dragging = false;
	}
	
	// Start drawing on the screen
	@Override
	public void mouseDragged(MouseEvent e) {
		// Check if mouse is not on white screen or the mouse is not pressed
		if(this.dragging == false) {
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		
	    // Check that mouse does not go out of the white board
		if(y < AppPanel.appHeight - 200 + 5 && x < AppPanel.appWidth - 200) {
			this.drawing.coordinates.add(new Coordinates(x, y));
			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
