package se.faerie.lighttest;

public class LightSourceMover {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public void moveLightSource(LightSource lightSource) {
		int newX = lightSource.getXPosition() + lightSource.getXVelocity();
		int newY = lightSource.getYPosition() + lightSource.getYVelocity();

		if (newX > maxX) {
			lightSource.setXVelocity(-Math.abs(lightSource.getXVelocity()));
		} else if (newX < minX) {
			lightSource.setXVelocity(Math.abs(lightSource.getXVelocity()));
		}
		if (newY > maxY) {
			lightSource.setYVelocity(-Math.abs(lightSource.getYVelocity()));
		} else if (newY < minY) {
			lightSource.setYVelocity(Math.abs(lightSource.getYVelocity()));
		}

		lightSource.setXPosition(newX);
		lightSource.setYPosition(newY);
	}

}
