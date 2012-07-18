package se.faerie.lighttest;


public class LightSourceLightMap implements LightMap {

	private LightSource[] sources;
	private int alpha;
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public LightSource[] getSources() {
		return sources;
	}

	public void setSources(LightSource[] sources) {
		this.sources = sources;
	}

	@Override
	public int getRGB(int x, int y) {
		double red = 0;
		double blue = 0;
		double green = 0;

		// add the light from each source
		for (int i = 0; i < sources.length; i++) {
			int xDiff = x - sources[i].getXPosition();
			int yDiff = y - sources[i].getYPosition();
			
			int distance = (xDiff * xDiff + yDiff * yDiff);
			if(distance > 30000){
				continue;
			}
			if (distance == 0) {
				distance = 1;
			}
			
			double fadeFactor = distance / sources[i].getLuminosity();
			
			//dampen at long distances to ease cutoff
			if(fadeFactor>2000){
				fadeFactor = fadeFactor*fadeFactor;
			}
			
			red += (double) (sources[i].getRed()) / fadeFactor;
			blue += (double) (sources[i].getBlue()) / fadeFactor;
			green += (double) (sources[i].getGreen()) / fadeFactor;

		}
		int redInt, blueInt, greenInt;

		// normalize if we get too high
		double max = Math.max(green, Math.max(red, blue));
		if (max > 1.0) {
			redInt = (int) ((double) (red / max) * (double) 255);
			blueInt = (int) ((double) (blue / max) * (double) 255);
			greenInt = (int) ((double) (green / max) * (double) 255);
		} else {
			redInt = (int) (red * 255);
			blueInt = (int) (blue * 255);
			greenInt = (int) (green * 255);
		}

		// we can't get blacker than black
		redInt = redInt < 0 ? 0 : redInt;
		blueInt = blueInt < 0 ? 0 : blueInt;
		greenInt = greenInt < 0 ? 0 : greenInt;
		
		 return (( alpha & 0xFF) << 24) |
         ((redInt & 0xFF) << 16) |
         ((greenInt & 0xFF) << 8)  |
         ((blueInt & 0xFF) << 0);
	}
}
