package se.faerie.lighttest;


public class LightSource {

	private double luminosity;
	private int red;
	private int blue;
	private int green;
	private int xPosition;
	private int yPosition;
	private int xVelocity;
	private int yVelocity;
	
	public double getLuminosity() {
		return luminosity;
	}
	public void setLuminosity(double luminosity) {
		this.luminosity = luminosity;
	}

	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getXPosition() {
		return xPosition;
	}
	public void setXPosition(int position) {
		xPosition = position;
	}
	public int getYPosition() {
		return yPosition;
	}
	public void setYPosition(int position) {
		yPosition = position;
	}
	public int getXVelocity() {
		return xVelocity;
	}
	public void setXVelocity(int velocity) {
		xVelocity = velocity;
	}
	public int getYVelocity() {
		return yVelocity;
	}
	public void setYVelocity(int velocity) {
		yVelocity = velocity;
	}
	
	
}
