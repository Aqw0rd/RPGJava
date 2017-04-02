package framework;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import Object.Player;
import UI.ActionBar;
import UI.ActionSlots;
import UI.HealthBar;
import UI.InvSlots;
import UI.Inventory;
import UI.ManaBar;
import mapGenerator.Handler;
import mapGenerator.ObjectId;
import mapGenerator.SimplexNoise;
import mapGenerator.TileSets;
import mapGenerator.Tiles;
import mapGenerator.UIHandler;
import mapGenerator.UIid;
import mapGenerator.Window;

public class Game extends Canvas implements Runnable {
	// -----------------------------Global vars--------------------------//
	// float[][] noiseDetail;
	int[][] noiseEl;
	int[][] mapArray;
	int[][] treeArray;
	//Tiles[][] tiles;
	
	Tiles[][] tile_ground;
	Tiles[][] tile_elevation;
	Tiles[][] details;
	Tiles[][] corners;
	Tiles[][] transition;
	
	BufferedImage[][] biome_images;
	BufferedImage[] baseImages;
	BufferedImage[][] transitions;
	BufferedImage[][] elevation;
	BufferedImage[] rocks;
	BufferedImage[] grass;
	BufferedImage[] trees; 
	
	Camera cam;
	int mapSize;
	
	TileSets treeTiles = new TileSets();
	TileSets rockTiles = new TileSets();
	TileSets surface = new TileSets();
	TileSets baseTiles = new TileSets();
	TileSets elTiles = new TileSets();
	TileSets biome = new TileSets();
	TileSets transitionTiles = new TileSets();
	TileSets grassTiles = new TileSets();
	
	
	int x, y;
	boolean update = true;
	Handler handler;
	UIHandler uiHandler;
	
	public static int WIDTH, HEIGHT;
	// -----------------------------------||----------------------------//

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
		handler = new Handler();
		uiHandler = new UIHandler();
		handler.addObject(new Player(100,100,ObjectId.Player));
		
		this.addKeyListener(new KeyInput(handler, uiHandler));
		
		WIDTH = getWidth();
		HEIGHT = getHeight();
		
		uiHandler.addObject(new ActionBar(100, HEIGHT-60, 600, 60,true, UIid.ActionBar));
		uiHandler.addObject(new HealthBar(495, HEIGHT-55, 200, 23, true, UIid.HealthBar));
		uiHandler.addObject(new ManaBar(495, HEIGHT-28, 200, 23, true, UIid.ManaBar));
		uiHandler.addObject(new Inventory(530, HEIGHT-235, 170, 170,false,UIid.Inventory));
		int i = 0;
		for(UIid id:UIid.actionSlots){
			uiHandler.addObject(new ActionSlots(i*55 + 105, HEIGHT-55, 50, 50, true, id));
			i++;
			System.out.println(i);
		}
		i=0;
		for(UIid id:UIid.invSlots){
				if(i<3) 		uiHandler.addObject(new InvSlots(i * 55 + 535, HEIGHT-230, 50, 50, true, id));
				else if(i<6) 	uiHandler.addObject(new InvSlots((i-3) * 55 + 535, (HEIGHT-230)+55, 50, 50, true, id));
				else 			uiHandler.addObject(new InvSlots((i-6) * 55 + 535, (HEIGHT-230)+110, 50, 50, true, id));
		i++;
		}
		
		//tiles = new Tiles[mapSize][mapSize];
		tile_ground = new Tiles[mapSize][mapSize];
		tile_elevation = new Tiles[mapSize][mapSize];
		details = new Tiles[mapSize][mapSize];
		corners = new Tiles[mapSize][mapSize];
		transition = new Tiles[mapSize][mapSize];
		
		surface.setImg("src/resources/surface.png");
		biome.setImg("src/resources/biome.png");
		baseTiles.setImg("src/resources/basetiles.png");
		transitionTiles.setImg("src/resources/transitions.png");
		elTiles.setImg("src/resources/elevation.png");
		rockTiles.setImg("src/resources/rocks.png");
		grassTiles.setImg("src/resources/grass.png");
		treeTiles.setImg("src/resources/tree.png");
		
		
		mapArray = generateNoise(mapSize, mapSize, 0, 2,0.003f);
		// noiseDetail = generateNoise(chunk,chunk,0,1);
		noiseEl = generateNoise(mapSize, mapSize, 0, 5,0.0003f);
		treeArray = generateNoise(mapSize, mapSize, 0, 1, 0.003f);
		
		addTiles();
		addProperties();
		loadImages();
		addDetails();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		setUp();
		this.requestFocus();
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
				render();
				frames++;	
			}
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
		for(int i = 0; i < handler.object.size(); i++){
			GameObject temp = handler.object.get(i);
			if(temp.getId() == ObjectId.Player){
				cam.tick(temp);
				for(int j = 0; j < uiHandler.object.size(); j++){
					UIObject uiTemp = uiHandler.object.get(j);
					if(uiTemp.id == UIid.HealthBar) uiTemp.size.x = uiTemp.fullSize.x * temp.hp/temp.maxHp;
					if(uiTemp.id == UIid.ManaBar) uiTemp.size.x = uiTemp.fullSize.x * temp.mana/temp.maxMana;
				}
			}
		}
		uiPos();
		uiHandler.tick();
		handler.tick();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3); // triplebuffering
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		// ------------DRAW HERE---------------------//
		g2d.translate(cam.pos.x, cam.pos.y);
		// --Affected by the camera--//

		updateTiles(g2d, Math.abs(cam.pos.x), Math.abs(cam.pos.y), this.getWidth() + Math.abs(cam.pos.x),
				this.getHeight() + Math.abs(cam.pos.y), 1);
		uiHandler.render(g);
		handler.render(g);
		// --Affected by the camera--//
		g2d.translate(-cam.pos.x, -cam.pos.y);

		// ------------DRAW HERE---------------------//
		g.dispose();
		bs.show();
	}

	public void loadImages() {

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
		
		rocks = new BufferedImage[rockTiles.getImg().getWidth() / 32];
		for(int i = 0; i < rockTiles.getImg().getWidth() / 32; i++){
			rocks[i] = rockTiles.getImg().getSubimage(i * 32, 0, 32, 32);
		}
		
		grass = new BufferedImage[grassTiles.getImg().getWidth() / 32];
		for(int i = 0; i < grassTiles.getImg().getWidth() / 32; i++){
			grass[i] = grassTiles.getImg().getSubimage(i * 32, 0, 32, 32);
		}
		
		trees = new BufferedImage[treeTiles.getImg().getWidth() / 32];
		for (int i = 0; i < treeTiles.getImg().getWidth() / 32; i++) {
			trees[i] = treeTiles.getImg().getSubimage(i * 32, 0, 32, 32);
		}
	}

	public void updateTiles(Graphics2D g, int w0, int h0, int w, int h, double s) {
		int a = (int) Math.floor(map(w0, 0, mapSize * 32, 0, mapSize));
		int b = (int) Math.floor(map(w, 0, mapSize * 32, 0, mapSize));

		int c = (int) Math.floor(map(h0, 0, mapSize * 32, 0, mapSize));
		int d = (int) Math.floor(map(h, 0, mapSize * 32, 0, mapSize));


		if (a > 0) a--;
		if (b < mapSize - 1) b++;
		if (c > 0) c--;
		if (d < mapSize - 1)d++;

		for (int i = a; i < b; i++) {
			for (int j = c; j < d; j++) {
				g.drawImage(baseImages[tile_ground[i][j].getId()], null, i * 32, j * 32);
				
				if(details[i][j].getId() == 1) g.drawImage(grass[0], null, i * 32, j * 32);
				if(details[i][j].getId() >= 2 && details[i][j].getId() <= 4 ) g.drawImage(rocks[details[i][j].getId()-1], null, i *32, j *32);
				if(details[i][j].getId() == 5){
					g.drawImage(trees[1], null, i *32, j *32);
					g.drawImage(trees[0], null, (i-1) *32, j *32);
					g.drawImage(trees[2], null, (i-1) *32, (j-1) *32);
					g.drawImage(trees[3], null, i *32, (j-1) *32);
					g.drawImage(trees[4], null, (i-1) *32, (j-2) *32);
					g.drawImage(trees[5], null, i * 32, (j-2) *32);
				}
				
				int dec = binaryToDec(transition[i][j].getUp() + transition[i][j].getDown() + transition[i][j].getLeft()
						+ transition[i][j].getRight());
				int elDec = binaryToDec(tile_elevation[i][j].getUp() + tile_elevation[i][j].getDown() + tile_elevation[i][j].getLeft()
						+ tile_elevation[i][j].getRight());
				
				if(transition[i][j].getId() >=0){
					if(transition[i][j].getType() == 1)
						g.drawImage(elevation[elDec - 1][transition[i][j].getId()], null, i * 32, j * 32);
					if(transition[i][j].getType() == 0)
						g.drawImage(transitions[dec - 1][transition[i][j].getId()-1], null, i * 32, j * 32);
				}
				
				elDec = binaryToDec(corners[i][j].getUp() + corners[i][j].getDown() + corners[i][j].getLeft()
						+ corners[i][j].getRight());
				
				//Draw Corners
				if (corners[i][j].getId() >=0){
					if (corners[i][j].getType() == 0){
						g.drawImage(transitions[elDec - 1][corners[i][j].getId()], null, i * 32, j * 32);
					}
					else if(corners[i][j].getType() == 1) {
						g.drawImage(elevation[elDec - 1][corners[i][j].getId()], null, i * 32, j * 32);
					}
				}
			}
		}
	}
	
	public void uiPos(){
		for(int i = 0; i < uiHandler.object.size(); i++){
			UIObject tempObject = uiHandler.object.get(i);
			
			//tempObject.size.x = tempObject.fullSize.x * this.getWidth()/WIDTH;
			//tempObject.size.y = tempObject.fullSize.y * this.getHeight()/HEIGHT;
			tempObject.pos.x = (tempObject.abspos.x + Math.abs(cam.pos.x));// * this.getWidth()/WIDTH;
			tempObject.pos.y = (tempObject.abspos.y + Math.abs(cam.pos.y));// * this.getHeight()/HEIGHT;
		}
	}
	
	public void addTiles() {
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				tile_ground[i][j] = new Tiles();
				tile_elevation[i][j] = new Tiles();
				details[i][j] = new Tiles();
				corners[i][j] = new Tiles();
				transition[i][j] = new Tiles();
				transition[i][j].setId(-1);
				corners[i][j].setId(-1);
				if (mapArray[i][j] < 2) {
					tile_ground[i][j].setId(1);
					tile_elevation[i][j].setId(0
							/*noiseEl[i][j]*/);
					//details[i][j].setId((treeArray[i][j]==1) ? 3 : 0);
					
				} else {
					tile_ground[i][j].setId(0);
					tile_elevation[i][j].setId(0);
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
				if (tile_ground[i][j].getId() == tile_ground[i][j - 1].getId())
					transition[i][j].setUp(0);
				else {
					if (tile_ground[i][j].getId() < tile_ground[i][j - 1].getId()) {
						transition[i][j].setUp(10);
						tile_ground[i][j - 1].setEdge(true);
						transition[i][j].setId(tile_ground[i][j - 1].getId());
						transition[i][j].setType(0);
					} else {
						tile_ground[i][j - 1].setEdge(false);
					}
				}

				if (tile_ground[i][j].getId() == tile_ground[i][j + 1].getId())
					transition[i][j].setDown(0);
				else {
					if (tile_ground[i][j].getId() < tile_ground[i][j + 1].getId()) {
						transition[i][j].setDown(1000);
						tile_ground[i][j + 1].setEdge(true);
						transition[i][j].setId(tile_ground[i][j + 1].getId());
						transition[i][j].setType(0);
					} else {
						tile_ground[i][j + 1].setEdge(false);
					}
				}

				if (tile_ground[i][j].getId() == tile_ground[i - 1][j].getId())
					transition[i][j].setLeft(0);
				else {
					
					if (tile_ground[i][j].getId() < tile_ground[i - 1][j].getId()) {
						transition[i][j].setLeft(1);
						tile_ground[i - 1][j].setEdge(true);
						transition[i][j].setId(tile_ground[i - 1][j].getId());
						transition[i][j].setType(0);
					} else {
						transition[i - 1][j].setEdge(false);
					}
				}

				if (tile_ground[i][j].getId() == tile_ground[i + 1][j].getId())
					transition[i][j].setRight(0);
				else {
					if (tile_ground[i][j].getId() < tile_ground[i + 1][j].getId()) {
						transition[i][j].setRight(100);
						transition[i][j].setType(0);
						tile_ground[i + 1][j].setEdge(true);
						transition[i][j].setId(tile_ground[i + 1][j].getId());
					} else {
						tile_ground[i + 1][j].setEdge(false);
					}
				}

				// ----------------ELEVATION--------------------//
				if (tile_elevation[i][j].getId() < tile_elevation[i][j - 1].getId()) {
					tile_elevation[i][j].setUp(10);
					transition[i][j].setId(0);
					tile_elevation[i][j - 1].setEdge(true);
					transition[i][j].setType(1);
				} else {
					tile_elevation[i][j].setUp(0);
				}

				if (tile_elevation[i][j].getId() < tile_elevation[i][j + 1].getId()) {
					tile_elevation[i][j].setDown(1000);
					transition[i][j].setId(0);
					tile_elevation[i][j + 1].setEdge(true);
					transition[i][j].setType(1);
				} else {
					tile_elevation[i][j].setDown(0);
				}

				if (tile_elevation[i][j].getId() < tile_elevation[i - 1][j].getId()) {
					tile_elevation[i][j].setLeft(1);
					transition[i][j].setId(0);
					tile_elevation[i - 1][j].setEdge(true);
					transition[i][j].setType(1);
				} else {
					tile_elevation[i][j].setLeft(0);
				}

				if (tile_elevation[i][j].getId() < tile_elevation[i + 1][j].getId()) {
					tile_elevation[i][j].setRight(100);
					transition[i][j].setId(0);
					tile_elevation[i + 1][j].setEdge(true);
					transition[i][j].setType(1);
				} else {
					tile_elevation[i][j].setRight(0);
				}

				// EDGES
				if (i - 1 == 0) {
					if (tile_ground[i - 1][j].getId() == tile_ground[i][j + 1].getId())
						tile_ground[i - 1][j].setDown(1000);
					else
						tile_ground[i - 1][j].setDown(1);

					if (tile_ground[i - 1][j].getId() == tile_ground[i + 1][j].getId())
						tile_ground[i - 1][j].setRight(100);
					else
						tile_ground[i - 1][j].setRight(1);

					if (tile_ground[i - 1][j].getId() == tile_ground[i][j - 1].getId())
						tile_ground[i - 1][j].setUp(10);
					else
						tile_ground[i - 1][j].setUp(0);

					tile_ground[i - 1][j].setLeft(1);

				}
				if (i + 1 == mapSize - 1) {
					if (tile_ground[i + 1][j].getId() == tile_ground[i][j - 1].getId())
						tile_ground[i + 1][j].setUp(10);
					else
						tile_ground[i + 1][j].setUp(0);

					if (tile_ground[i + 1][j].getId() == tile_ground[i - 1][j].getId())
						tile_ground[i + 1][j].setLeft(1);
					else
						tile_ground[i + 1][j].setLeft(0);

					if (tile_ground[i + 1][j].getId() == tile_ground[i][j + 1].getId())
						tile_ground[i + 1][j].setDown(1000);
					else
						tile_ground[i + 1][j].setDown(0);

					tile_ground[i + 1][j].setRight(100);
				}
				if (j - 1 == 0) {
					if (tile_ground[i][j - 1].getId() == tile_ground[i - 1][j].getId())
						tile_ground[i][j - 1].setLeft(1);
					else
						tile_ground[i][j - 1].setLeft(1);

					if (tile_ground[i][j - 1].getId() == tile_ground[i][j + 1].getId())
						tile_ground[i][j - 1].setDown(1000);
					else
						tile_ground[i][j - 1].setDown(0);

					if (tile_ground[i][j - 1].getId() == tile_ground[i + 1][j].getId())
						tile_ground[i][j - 1].setRight(100);
					else
						tile_ground[i][j - 1].setRight(0);

					tile_ground[i][j - 1].setUp(10);
				}
				if (j + 1 == mapSize - 1) {

					if (tile_ground[i][j + 1].getId() == tile_ground[i][j - 1].getId())
						tile_ground[i][j + 1].setUp(10);
					else
						tile_ground[i][j + 1].setUp(0);

					if (tile_ground[i][j + 1].getId() == tile_ground[i + 1][j].getId())
						tile_ground[i][j + 1].setRight(100);
					else
						tile_ground[i][j + 1].setRight(100);

					if (tile_ground[i][j + 1].getId() == tile_ground[i - 1][j].getId())
						tile_ground[i][j + 1].setLeft(1);
					else
						tile_ground[i][j + 1].setLeft(0);

					tile_ground[i][j + 1].setDown(1000);

				}

			}
		}

		// Corners
		for (int i = 1; i < mapSize - 1; i++) {
			for (int j = 1; j < mapSize - 1; j++) {
				if (tile_ground[i][j].isEdge()) {
					if (tile_ground[i][j - 1].getId() < tile_ground[i][j].getId()
					&&  tile_ground[i - 1][j].getId() < tile_ground[i][j].getId()) { // SOUTHEAST(100)
						corners[i - 1][j - 1].setId(tile_ground[i- 1][j - 1].getId()+1);
						corners[i - 1][j - 1].setUp(100);
						corners[i - 1][j - 1].setType(0);
					}

					if (tile_ground[i][j - 1].getId() < tile_ground[i][j].getId()
							&& tile_ground[i + 1][j].getId() < tile_ground[i][j].getId()) { // SOUTHWEST(1000)
						corners[i + 1][j - 1].setId(tile_ground[i + 1][j - 1].getId()+1);
						corners[i + 1][j - 1].setDown(1000);
						corners[i + 1][j - 1].setType(0);
					}
					
					if (tile_ground[i][j + 1].getId() < tile_ground[i][j].getId()
							&& tile_ground[i - 1][j].getId() < tile_ground[i][j].getId()) { // NORTHEAST(10)
						corners[i - 1][j + 1].setId(tile_ground[i-1][j+1].getId()+1);
						corners[i - 1][j + 1].setLeft(10);
						corners[i - 1][j + 1].setType(0);
					}
					
					if (tile_ground[i][j + 1].getId() < tile_ground[i][j].getId()
							&& tile_ground[i + 1][j].getId() < tile_ground[i][j].getId()) { // NORTHWEST(1)
						corners[i + 1][j + 1].setId(tile_ground[i+1][j+1].getId()+1);
						corners[i + 1][j + 1].setRight(1);
						corners[i + 1][j + 1].setType(0);
					}
				}
				//Elevated Corners
				if (tile_elevation[i][j].isEdge()) {
					if (tile_elevation[i][j - 1].getId() < tile_elevation[i][j].getId()
					&&  tile_elevation[i - 1][j].getId() < tile_elevation[i][j].getId()) { // SOUTHEAST(100)
						corners[i - 1][j - 1].setId(tile_ground[i][j].getId());
						corners[i - 1][j - 1].setUp(100);
						corners[i - 1][j - 1].setType(1);
					}

					if (tile_elevation[i][j - 1].getId() < tile_elevation[i][j].getId()
					&& tile_elevation[i + 1][j].getId() < tile_elevation[i][j].getId()) { // SOUTHWEST(1000)
						corners[i + 1][j - 1].setId(tile_ground[i][j].getId());
						corners[i + 1][j - 1].setDown(1000);
						corners[i + 1][j - 1].setType(1);
					}
					
					if (tile_elevation[i][j + 1].getId() < tile_elevation[i][j].getId()
					&& tile_elevation[i - 1][j].getId() < tile_elevation[i][j].getId()) { // NORTHEAST(10)
						corners[i - 1][j + 1].setId(tile_ground[i][j].getId());
						corners[i - 1][j + 1].setLeft(10);
						corners[i - 1][j + 1].setType(1);
					}
					
					if (tile_elevation[i][j + 1].getId() < tile_elevation[i][j].getId()
					&& tile_elevation[i + 1][j].getId() < tile_elevation[i][j].getId()) { // NORTHWEST(1)
						corners[i + 1][j + 1].setId(tile_ground[i][j].getId());
						corners[i + 1][j + 1].setRight(1);
						corners[i + 1][j + 1].setType(1);
					}
				}
			}
		}
	}

	public void addDetails(){
		Random rg = new Random();
		int r = 0;
		for (int i = 1; i < mapSize-1; i++) {
			for (int j = 1; j < mapSize-1; j++) {
				if(tile_ground[i][j].getId() == 1 /*&& details[i][j].getId() != 3*/){
					r = rg.nextInt(100)+1;
					if(r > 99) details[i][j].setId(5);
					else{
						r = rg.nextInt(6);
						int g1 = (details[i-1][j].getId() == 1) ? 1 : 0;
						int g2 = (details[i][j-1].getId() == 1) ? 1 : 0;
						r += g2 + g1;
						if(r > 4) details[i][j].setId(1);
						else{
							details[i][j].setId(0);
							r = rg.nextInt(100);
							if(r>98) details[i][j].setId(rg.nextInt(3)+2);
						}
					}
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
		new Window(800, 660, "Map generator", new Game());
	}

}
