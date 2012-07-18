package se.faerie.lighttest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JFrame;

public class LightTest {

	private static int xSize = 800;
	private static int ySize = 600;
	private static int maxLuminosity = 60;
	private static int maxColor = 30;
	private static int maxVelocity = 2;
	private static int numberOfSources = 10;
	private static int numberOfThreads = 4;
	private static int alphaBlending = 255;

	private static Random random = new Random();
	
	public static void main(String[] args) {

		LightSource[] sources = new LightSource[numberOfSources];

		for (int i = 0; i < numberOfSources; i++) {
			int lum = random.nextInt(maxLuminosity);
			int red = random.nextInt(maxColor);
			int blue = random.nextInt(maxColor);
			int green = random.nextInt(maxColor);
			int x = random.nextInt(xSize);
			int y = random.nextInt(xSize);
			int xPrim = random.nextInt(maxVelocity);
			int yPrim = random.nextInt(maxVelocity);

			LightSource source = new LightSource();
			source.setBlue(blue);
			source.setGreen(green);
			source.setLuminosity(lum);
			source.setRed(red);
			source.setXPosition(x);
			source.setYPosition(y);
			source.setXVelocity(xPrim);
			source.setYVelocity(yPrim);
			sources[i] = source;
		}

		LightSourceLightMap lightMap = new LightSourceLightMap();
		lightMap.setSources(sources);
		lightMap.setAlpha(alphaBlending);

		// startUsingBufferedImage(lightMap, sources);
		startUsingInMemoryImage(lightMap, sources);
	}

	private static void startUsingInMemoryImage(LightMap lightMap,
			LightSource[] sources) {
		int[] pixelsOne = new int[xSize * ySize];
		int[] pixelsTwo = new int[xSize * ySize];
		int[] writeArray = pixelsOne;
		int[] bufferArray = pixelsTwo;
		int[] tmpArray = null;

		MemoryImageSource imageSource = new MemoryImageSource(xSize, ySize,
				writeArray, 0, xSize);
		imageSource.setAnimated(false);
		imageSource.setFullBufferUpdates(false);

		CyclicBarrier drawBarrier = new CyclicBarrier(numberOfThreads + 1);
		CyclicBarrier updateBarrier = new CyclicBarrier(numberOfThreads + 1);

		InMemoryImageLightMapWriter[] drawers = new InMemoryImageLightMapWriter[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			InMemoryImageLightMapWriter drawer = new InMemoryImageLightMapWriter();
			drawer.setMinY(i * ySize / numberOfThreads);
			drawer.setMinX(0);
			drawer.setMaxY((i + 1) * ySize / numberOfThreads);
			drawer.setMaxX(xSize);
			drawer.setLightMap(lightMap);
			drawer.setPixels(bufferArray);
			drawer.setPixelOffSet(i * ySize * xSize / numberOfThreads);
			drawer.setDrawBarrier(drawBarrier);
			drawer.setUpdateBarrier(updateBarrier);
			drawers[i] = drawer;
		}

		LightSourceMover mover = new LightSourceMover();
		mover.setMinX(0);
		mover.setMinY(0);
		mover.setMaxX(xSize);
		mover.setMaxY(ySize);

		JFrame f = new JFrame("Lighttest");
		f.setVisible(true);
		f.setSize(xSize, ySize);
		f.getGraphics().setFont(new Font(Font.SANS_SERIF, 11, Font.PLAIN));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		LightSourceMouseListener lightSourceMouseListener = new LightSourceMouseListener();
		lightSourceMouseListener.setSources(sources);
		f.addMouseListener(lightSourceMouseListener);
		f.addMouseMotionListener(lightSourceMouseListener);
		f.addMouseWheelListener(lightSourceMouseListener);

		int fps = 0;
		long totalTime = 0;
		int numberOfFrames = 0;
		Thread[] threads = new Thread[numberOfThreads];

		for (int i = 0; i < numberOfThreads; i++) {
			Thread t = new Thread(drawers[i]);
			t.start();
			threads[i] = t;
		}

		while (true) {
			try {
				long time = System.nanoTime();
				
				drawBarrier.await();
				
				Image currentImage = Toolkit.getDefaultToolkit().createImage(
						imageSource);

				Graphics2D graphics = (Graphics2D) f.getGraphics();

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

				graphics.drawImage(currentImage, 0, 0, null);
				graphics.setColor(Color.white);
				graphics.drawString("Frames last second: " + fps, 10, 50);
				Toolkit.getDefaultToolkit().sync();


				tmpArray = writeArray;
				writeArray = bufferArray;
				bufferArray = tmpArray;

				LightSource currentSource = lightSourceMouseListener
						.getCurrentSource();
				if (currentSource != null) {

					currentSource.setXPosition(currentSource.getXPosition()
							+ lightSourceMouseListener.getMoveX());
					currentSource.setYPosition(currentSource.getYPosition()
							+ lightSourceMouseListener.getMoveY());

					currentSource.setXVelocity(lightSourceMouseListener
							.getMoveX() / 3);
					currentSource.setYVelocity(lightSourceMouseListener
							.getMoveY() / 3);

					currentSource.setLuminosity(currentSource.getLuminosity()
							* lightSourceMouseListener.getLuminosityModifier());
					lightSourceMouseListener.clear();
				} else {
					for (int i = 0; i < numberOfSources; i++) {
						mover.moveLightSource(sources[i]);
					}
				}

	

				totalTime += (System.nanoTime() - time);
				numberOfFrames++;
				if (totalTime > 1000000000) {
					fps = numberOfFrames;
					totalTime = totalTime - 1000000000;
					numberOfFrames = 0;
				}

				currentImage.flush();
				imageSource.newPixels(writeArray, ColorModel.getRGBdefault(),
						0, xSize);
				updateBarrier.await();

			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
