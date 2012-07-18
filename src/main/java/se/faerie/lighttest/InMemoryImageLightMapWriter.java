package se.faerie.lighttest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class InMemoryImageLightMapWriter implements Runnable {

	private int pixelOffSet;
	private int[] pixels;
	private LightMap lightMap;
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private boolean run=true;
	private CyclicBarrier drawBarrier;
	private CyclicBarrier updateBarrier;
	
	public void setDrawBarrier(CyclicBarrier drawBarrier) {
		this.drawBarrier = drawBarrier;
	}

	public void setUpdateBarrier(CyclicBarrier updateBarrier) {
		this.updateBarrier = updateBarrier;
	}

	public void setPixelOffSet(int pixelOffSet) {
		this.pixelOffSet = pixelOffSet;
	}

	public void setLightMap(LightMap lightMap) {
		this.lightMap = lightMap;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	@Override
	public void run() {
		while (run) {
			int index = pixelOffSet;
			for (int y = minY; y < maxY; y++) {
				for (int x = minX; x < maxX; x++) {
					pixels[index++] = lightMap.getRGB(x, y);
				}
			}
			try {
				drawBarrier.await();
				updateBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}

}
