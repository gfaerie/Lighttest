package se.faerie.lighttest;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class LightSourceMouseListener implements MouseListener,
		MouseMotionListener, MouseWheelListener {

	private LightSource[] sources;
	private LightSource currentSource;
	private int lastXClicked = 0;
	private int lastYClicked = 0;
	private int currentXpos = 0;
	private int currentYpos = 0;
	private int moveX = 0;
	private int moveY = 0;
	private double luminosityModifier = 1;


	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public void setSources(LightSource[] sources) {
		this.sources = sources;
	}

	public LightSource getCurrentSource() {
		return currentSource;
	}

	public double getLuminosityModifier() {
		return luminosityModifier;
	}

	public void clear() {
		luminosityModifier = 1.0;
		moveX = 0;
		moveY = 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
			setLastLocationClicked(e);
			setCurrentSource(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clear();
		currentSource = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		updateCurrentLocation(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		luminosityModifier += 0.15 * e.getWheelRotation();

	}

	private void setLastLocationClicked(MouseEvent e) {
		lastXClicked = e.getX();
		lastYClicked = e.getY();
		currentXpos = lastXClicked;
		currentYpos = lastYClicked;
	}

	private void updateCurrentLocation(MouseEvent e) {
		moveX += e.getX() - currentXpos;
		moveY += e.getY() - currentYpos;
		currentXpos = e.getX();
		currentYpos = e.getY();
	}

	private void setCurrentSource(MouseEvent e) {
		int currentDistance = Integer.MAX_VALUE;
		for (int i = 0; i < sources.length; i++) {
			int xDistance = lastXClicked - sources[i].getXPosition();
			int yDistance = lastYClicked - sources[i].getYPosition();
			int totalDistance = xDistance * xDistance + yDistance * yDistance;
			if (totalDistance < currentDistance) {
				currentDistance = totalDistance;
				currentSource = sources[i];
			}
		}
	}

}
