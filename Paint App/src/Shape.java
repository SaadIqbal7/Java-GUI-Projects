import java.awt.Color;
import java.util.ArrayList;

public class Shape {
	public ShapeType shapeType;
	public ArrayList<Coordinates> coordinates;
	public Color color;
	public Float thickness;
	
	public Shape(ShapeType shapeType, Color color, Float thickness) {
		this.shapeType = shapeType;
		this.coordinates = new ArrayList<Coordinates>();
		this.color = color;
		this.thickness = thickness;
	}
}
