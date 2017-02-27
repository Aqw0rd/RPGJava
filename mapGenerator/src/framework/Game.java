package framework;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import mapGenerator.SimplexNoise;
import mapGenerator.TileSets;
import mapGenerator.Tiles;
import mapGenerator.Window;

public class Game extends Canvas implements Runnable {
	// -----------------------------Global vars--------------------------//
	// float[][] noiseDetail;
	int[][] noiseEl;
	int[][] mapArray;
	Tiles[][] tiles;
	BufferedImage[][] biome_images;
	BufferedImage[] baseImages;
	BufferedImage[][] transitions;
	BufferedImage[][] elevation;
	Camera cam;
	int mapSize;
	TileSets surface = new TileSets();
	TileSets baseTiles = new TileSets();
	TileSets elTiles = new TileSets();
	TileSets biome = new TileSets();
	TileSets transitionTiles = new TileSets();
	int x, y;
	boolean update = true;
	// -----------------------------------||----------------------------//

	/**
	 * 
	 */
	private static final long serialVersionUID = -2413771048344743376L;

	private boolean running = false;
	private Thread thread;

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void setUp() {
		mapSize = 500;

		x = 0;
		y = 0;

		cam = new Camera();
		tiles = new Tiles[mapSize][mapSize];
		surface.setImg("src/resources/surface.png");
		biome.setImg("src/resources/biome.png");
		baseTiles.setImg("src/resources/basetiles.png");
		transitionTiles.setImg("src/resources/transitions.png");
		elTiles.setImg("src/resources/elevation.png");
		mapArray = generateNoise(mapSize, mapSize, 0, 2,0.003f);
		// noiseDetail = generateNoise(chunk,chunk,0,1);
		noiseEl = generateNoise(mapSize, mapSize, 0, 5,0.0003f);
		addTiles();
		addProperties();
		loadImages();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setUp();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				System.out.println("X: " + x + "Y: " + y);
				frames = 0;
				updates = 0;
			}
		}
	}

	private void tick() {
		cam.tick(x, y);
		x -= 1;
		y -= 1;
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(4); // doubleebuffering
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		// ------------DRAW HERE---------------------//
		g2d.translate(cam.pos.x, cam.pos.y);
		// --Affected by the camera--//

		updateTiles(g2d, Math.abs(cam.pos.x), Math.abs(cam.pos.y), this.getWidth() + Math.abs(cam.pos.x),
				this.getHeight() + Math.abs(cam.pos.y), 1);

		// --Affected by the camera--//
		g2d.translate(-cam.pos.x, -cam.pos.y);

		// ------------DRAW HERE---------------------//
		g.dispose();
		bs.show();
	}

	public void loadImages() {
		/*
		 * biome_images = new
		 * BufferedImage[biome.getImg().getWidth()/32][biome.getImg().getHeight(
		 * )/32]; for (int i = 0; i < biome.getImg().getWidth()/32; i++) { for
		 * (int j = 0; j < biome.getImg().getHeight()/32; j++) {
		 * biome_images[i][j] = biome.getImg().getSubimage(i*32, j*32, 32, 32);
		 * } }
		 */
		baseImages = new BufferedImage[baseTiles.getImg().getWidth() / 32];
		for (int i = 0; i < baseTiles.getImg().getWidth() / 32; i++) {
			baseImages[i] = baseTiles.getImg().getSubimage(i * 32, 0, 32, 32);
		}

		transitions = new BufferedImage[transitionTiles.getImg().getWidth() / 32][transitionTiles.getImg().getHeight()
				/ 32];
		for (int i = 0; i < transitionTiles.getImg().getWidth() / 32; i++) {
			for (int j = 0; j < transitionTiles.getImg().getHeight() / 32; j++) {
				transitions[i][j] = transitionTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
			}
		}

		elevation = new BufferedImage[elTiles.getImg().getWidth() / 32][elTiles.getImg().getHeight() / 32];
		for (int i = 0; i < elTiles.getImg().getWidth() / 32; i++) {
			for (int j = 0; j < elTiles.getImg().getHeight() / 32; j++) {
				elevation[i][j] = elTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
			}

		}

	}

	void updateTiles(Graphics2D g, int w0, int h0, int w, int h, double s) {
		int a = (int) Math.floor(map(w0, 0, mapSize * 32, 0, mapSize));
		int b = (int) Math.floor(map(w, 0, mapSize * 32, 0, mapSize));
		;
		int c = (int) Math.floor(map(h0, 0, mapSize * 32, 0, mapSize));
		int d = (int) Math.floor(map(h, 0, mapSize * 32, 0, mapSize));
		;

		if (a > 0)
			a--;
		if (b < mapSize - 1)
			b++;
		if (c > 0)
			c--;
		if (d < mapSize - 1)
			d++;

		for (int i = a; i < b; i++) {
			for (int j = c; j < d; j++) {
				g.drawImage(baseImages[tiles[i][j].getId()], null, i * 32, j * 32);
				if (tiles[i][j].isTransition() || tiles[i][j].isCorner()) {
					int dec = binaryToDec(tiles[i][j].getUp() + tiles[i][j].getDown() + tiles[i][j].getLeft()
							+ tiles[i][j].getRight());
					// System.out.println("UP: " + tiles[i][j].getUp() + " DOWN:
					// " + tiles[i][j].getDown() + " LEFT: " +
					// tiles[i][j].getLeft() + " RIGHT: " +
					// tiles[i][j].getRight() + " CORNER: " +
					// tiles[i][j].isCorner() + " TRANSITION: " +
					// tiles[i][j].isTransition());
					g.drawImage(transitions[dec - 1][tiles[i][j].getTransitionId() - 1], null, i * 32, j * 32);
				}
				if (tiles[i][j].isElTransition()) {
					int dec = binaryToDec(tiles[i][j].getElUp() + tiles[i][j].getElDown() + tiles[i][j].getElLeft()
							+ tiles[i][j].getElRight());
					g.drawImage(elevation[dec - 1][tiles[i][j].getTransitionId() - 1], null, i * 32, j * 32);
				}
			}
		}
	}

	public void addTiles() {
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				tiles[i][j] = new Tiles();
				if (mapArray[i][j] < 2) {
					tiles[i][j].setId(1);
					tiles[i][j].setElevation(noiseEl[i][j]);
				} else {
					tiles[i][j].setId(0);
					tiles[i][j].setElevation(0);
				}
			}
		}
	}

	private int[][] generateNoise(int width, int height, int a, int b, float layerF) {
		new SimplexNoise(new Random().nextInt(10000));
		int[][] noise = new int[width][height];
		// Frequency = features. Higher = more features
		// Weight = smoothness. Higher frequency = more smoothness
		float weight = 1f;

		for (int i = 0; i < 3; i++) {
			float ns = 0.f;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					ns += (float) SimplexNoise.noise(x * layerF, y * layerF) * weight;
					ns = clamp(ns, -1.0f, 1.0f);
					noise[x][y] = (int) Math.abs(Math.floor(map(ns, -1.0f, 1.0f, a, b)));
				}
			}
			layerF *= 3.5f;
			weight *= 0.5f;
		}

		return noise;
	}

	public void addProperties() {
		for (int i = 1; i < mapSize - 1; i++) {
			for (int j = 1; j < mapSize - 1; j++) {
				// Checking if tiles next to itself are equal to itself
				if (tiles[i][j].getId() == tiles[i][j - 1].getId())
					tiles[i][j].setUp(0);
				else {
					tiles[i][j].setUp(10);
					if (tiles[i][j].getId() < tiles[i][j - 1].getId()) {
						tiles[i][j].setTransition(true);
						tiles[i][j - 1].setTransition(false);
						tiles[i][j - 1].setEdge(true);
						tiles[i][j].setTransitionId(tiles[i][j - 1].getId());
					} else {
						tiles[i][j].setTransition(false);
						tiles[i][j - 1].setEdge(false);
						tiles[i][j].setTransitionId(-1);
					}
				}

				if (tiles[i][j].getId() == tiles[i][j + 1].getId())
					tiles[i][j].setDown(0);
				else {
					tiles[i][j].setDown(1000);
					if (tiles[i][j].getId() < tiles[i][j + 1].getId()) {
						tiles[i][j].setTransition(true);
						tiles[i][j + 1].setTransition(false);
						tiles[i][j + 1].setEdge(true);
						tiles[i][j].setTransitionId(tiles[i][j + 1].getId());
					} else {
						tiles[i][j].setTransition(false);
						tiles[i][j + 1].setEdge(false);
						tiles[i][j].setTransitionId(-1);
					}
				}

				if (tiles[i][j].getId() == tiles[i - 1][j].getId())
					tiles[i][j].setLeft(0);
				else {
					tiles[i][j].setLeft(1);
					if (tiles[i][j].getId() < tiles[i - 1][j].getId()) {
						tiles[i][j].setTransition(true);
						tiles[i - 1][j].setTransition(false);
						tiles[i - 1][j].setEdge(true);
						tiles[i][j].setTransitionId(tiles[i - 1][j].getId());
					} else {
						tiles[i][j].setTransition(false);
						tiles[i - 1][j].setEdge(false);
						tiles[i][j].setTransitionId(-1);
					}
				}

				if (tiles[i][j].getId() == tiles[i + 1][j].getId())
					tiles[i][j].setRight(0);
				else {
					tiles[i][j].setRight(100);
					if (tiles[i][j].getId() < tiles[i + 1][j].getId()) {
						tiles[i][j].setTransition(true);
						tiles[i + 1][j].setTransition(false);
						tiles[i + 1][j].setEdge(true);
						tiles[i][j].setTransitionId(tiles[i + 1][j].getId());
					} else {
						tiles[i][j].setTransition(false);
						tiles[i + 1][j].setEdge(false);
						tiles[i][j].setTransitionId(-1);
					}
				}

				// ----------------ELEVATION--------------------//
				if (tiles[i][j].getElevation() < tiles[i][j - 1].getElevation()) {
					tiles[i][j].setElUp(10);
					tiles[i][j].setElTransition(true);
					tiles[i][j].setTransitionId(1);
				} else {
					tiles[i][j].setElUp(0);
				}

				if (tiles[i][j].getElevation() < tiles[i][j + 1].getElevation()) {
					tiles[i][j].setElDown(1000);
					tiles[i][j].setElTransition(true);
					tiles[i][j].setTransitionId(1);
				} else {
					tiles[i][j].setElDown(0);
				}

				if (tiles[i][j].getElevation() < tiles[i - 1][j].getElevation()) {
					tiles[i][j].setElLeft(1);
					tiles[i][j].setElTransition(true);
					tiles[i][j].setTransitionId(1);
				} else {
					tiles[i][j].setElLeft(0);
				}

				if (tiles[i][j].getElevation() < tiles[i + 1][j].getElevation()) {
					tiles[i][j].setElRight(100);
					tiles[i][j].setElTransition(true);
					tiles[i][j].setTransitionId(1);
				} else {
					tiles[i][j].setElRight(0);
				}

				// EDGES
				if (i - 1 == 0) {
					if (tiles[i - 1][j].getId() == tiles[i][j + 1].getId())
						tiles[i - 1][j].setDown(1000);
					else
						tiles[i - 1][j].setDown(1);

					if (tiles[i - 1][j].getId() == tiles[i + 1][j].getId())
						tiles[i - 1][j].setRight(100);
					else
						tiles[i - 1][j].setRight(1);

					if (tiles[i - 1][j].getId() == tiles[i][j - 1].getId())
						tiles[i - 1][j].setUp(10);
					else
						tiles[i - 1][j].setUp(0);

					tiles[i - 1][j].setLeft(1);

				}
				if (i + 1 == mapSize - 1) {
					if (tiles[i + 1][j].getId() == tiles[i][j - 1].getId())
						tiles[i + 1][j].setUp(10);
					else
						tiles[i + 1][j].setUp(0);

					if (tiles[i + 1][j].getId() == tiles[i - 1][j].getId())
						tiles[i + 1][j].setLeft(1);
					else
						tiles[i + 1][j].setLeft(0);

					if (tiles[i + 1][j].getId() == tiles[i][j + 1].getId())
						tiles[i + 1][j].setDown(1000);
					else
						tiles[i + 1][j].setDown(0);

					tiles[i + 1][j].setRight(100);
				}
				if (j - 1 == 0) {
					if (tiles[i][j - 1].getId() == tiles[i - 1][j].getId())
						tiles[i][j - 1].setLeft(1);
					else
						tiles[i][j - 1].setLeft(1);

					if (tiles[i][j - 1].getId() == tiles[i][j + 1].getId())
						tiles[i][j - 1].setDown(1000);
					else
						tiles[i][j - 1].setDown(0);

					if (tiles[i][j - 1].getId() == tiles[i + 1][j].getId())
						tiles[i][j - 1].setRight(100);
					else
						tiles[i][j - 1].setRight(0);

					tiles[i][j - 1].setUp(10);
				}
				if (j + 1 == mapSize - 1) {

					if (tiles[i][j + 1].getId() == tiles[i][j - 1].getId())
						tiles[i][j + 1].setUp(10);
					else
						tiles[i][j + 1].setUp(0);

					if (tiles[i][j + 1].getId() == tiles[i + 1][j].getId())
						tiles[i][j + 1].setRight(100);
					else
						tiles[i][j + 1].setRight(100);

					if (tiles[i][j + 1].getId() == tiles[i - 1][j].getId())
						tiles[i][j + 1].setLeft(1);
					else
						tiles[i][j + 1].setLeft(0);

					tiles[i][j + 1].setDown(1000);

				}

			}
		}

		// Corners
		for (int i = 1; i < mapSize - 1; i++) {
			for (int j = 1; j < mapSize - 1; j++) {
				if (tiles[i][j].isEdge()) {
					if (tiles[i][j - 1].getId() < tiles[i][j].getId()
							&& tiles[i - 1][j].getId() < tiles[i][j].getId()) { // SOUTHEAST(100)
						tiles[i - 1][j - 1].setTransitionId(tiles[i][j].getId() + 1);
						tiles[i - 1][j - 1].setUp(100);
						tiles[i - 1][j - 1].setCorner(true);
					}
					/*
					 * else{ tiles[i-1][j-1].setUp(0); }
					 */

					if (tiles[i][j - 1].getId() < tiles[i][j].getId()
							&& tiles[i + 1][j].getId() < tiles[i][j].getId()) { // SOUTHWEST(1000)
						tiles[i + 1][j - 1].setTransitionId(tiles[i][j].getId() + 1);
						tiles[i + 1][j - 1].setDown(1000);
						tiles[i + 1][j - 1].setCorner(true);
					} /*
						 * else{ tiles[i+1][j-1].setDown(0); }
						 */

					if (tiles[i][j + 1].getId() < tiles[i][j].getId()
							&& tiles[i - 1][j].getId() < tiles[i][j].getId()) { // NORTHEAST(10)
						tiles[i - 1][j + 1].setTransitionId(tiles[i][j].getId() + 1);
						tiles[i - 1][j + 1].setLeft(10);
						tiles[i - 1][j + 1].setCorner(true);
					} /*
						 * else{ tiles[i-1][j+1].setLeft(0); }
						 */

					if (tiles[i][j + 1].getId() < tiles[i][j].getId()
							&& tiles[i + 1][j].getId() < tiles[i][j].getId()) { // NORTHWEST(1)
						tiles[i + 1][j + 1].setTransitionId(tiles[i][j].getId() + 1);
						tiles[i + 1][j + 1].setRight(1);
						tiles[i + 1][j + 1].setCorner(true);
					} /*
						 * else{ tiles[i+1][j+1].setRight(0); }
						 */
				}
			}
		}
	}

	public float clamp(float x, float min, float max) {
		if (x < min)
			return min;
		else if (x > max)
			return max;
		else
			return x;
	}

	public float map(float x, float a, float b, float c, float d) {
		return (x - a) / (b - a) * (d - c);
	}

	public int binaryToDec(int bin) {
		int dec = 0;
		String str = Integer.toString(bin);
		for (int i = 0; i < str.length(); i++) {
			int a = Integer.parseInt(String.valueOf(str.charAt(i)));
			dec = dec * 2 + a;
		}
		return dec;
	}

	public static void main(String args[]) {
		new Window(800, 800, "Map generator", new Game());
	}

}
