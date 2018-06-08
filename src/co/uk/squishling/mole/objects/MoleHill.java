package co.uk.squishling.mole.objects;

import co.uk.squishling.mole.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MoleHill {
	private double x;
	private double y;
	private double size;
	private boolean isMole = false;
	
	public MoleHill(double x, double y, double size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void draw(GraphicsContext gc) {
		// Hole
		gc.setFill(Color.BLACK);
		gc.fillRect(x - (size / 2), y - (size / 2), size, size);
		
		if (isMole) {
			// Mole
			gc.setFill(Color.BROWN);
			gc.fillRect(x - (size / 3), y - (size), (size / 3) * 2, size + (size / 3));
		} else {
			// Mole-Free Hole
			gc.setFill(Color.GRAY);
			gc.fillRect(x - (size / 3), y - (size / 3), (size / 3) * 2, (size / 3) * 2);
		}
	}
	
	public boolean clicked(double x, double y) {
		if (isMole && x > this.x - (size / 3) && x < (this.x - (size / 3)) + ((size / 3) * 2) && y > this.y - size && y < (this.y - size) + (size + (size / 3))) {
			return true;
		}
		
		return false;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setSize(double d) {
		this.size = d;
	}
	
	public void setIsMole(boolean isMole) {
		this.isMole = isMole;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getSize() {
		return size;
	}
	
	public boolean getIsMole() {
		return isMole;
	}
}
