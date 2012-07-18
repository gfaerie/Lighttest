package se.faerie.lighttest;


public class StaticLightMap implements LightMap {

	private int redInt=10;
	private int greenInt=100;
	private int blueInt=10;

	@Override
	public int getRGB(int x, int y) {
		return ((1 & 0xFF) << 24) |
        ((redInt & 0xFF) << 16) |
        ((greenInt & 0xFF) << 8)  |
        ((blueInt & 0xFF) << 0);
	}

	
	
}
