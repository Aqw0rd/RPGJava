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
	//-----------------------------Global vars--------------------------//
		float[][] noiseDetail;
		float[][] noiseEl;
		float[][] mapArray;
		Tiles[][] tiles;
		Tiles[][] biomes;
		BufferedImage[][] biome_images;
		Camera cam;
		int chunk;
		int biomeSize;
		int mapSize;
	    TileSets surface = new TileSets();
	    TileSets biome = new TileSets();
	    int x,y;
	    boolean update = true;
	//-----------------------------------||----------------------------//
	    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2413771048344743376L;
	
	private boolean running = false;
	private Thread thread;

	public synchronized void start(){
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void setUp(){
    	chunk = 50;
    	biomeSize = 10;
    	mapSize = 10;
    	
    	x = 0;
    	y = 0;
    	cam = new Camera();
    	tiles = new Tiles[chunk][chunk];
    	biomes = new Tiles[chunk][chunk];
    	surface.setImg("src/surface.png");	
    	biome.setImg("src/biome.png");
    	mapArray = generateNoise(10,10,0,2);
    	noiseDetail = generateNoise(chunk,chunk,0,1);
    	noiseEl = generateNoise(chunk,chunk,0,5);
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
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	private void tick(){
		cam.tick(x, y);
		x--;
    	y--;
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null){
			this.createBufferStrategy(4); //doubleebuffering
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		//------------DRAW HERE---------------------//
		g2d.translate(cam.pos.x, cam.pos.y);
    	//--Affected by the camera--// 
		
    	updateTiles(g2d, Math.abs(cam.pos.x) - 32, Math.abs(cam.pos.y) - 32,
    				this.getWidth() + Math.abs(cam.pos.x), this.getHeight() + Math.abs(cam.pos.y),
    				1);
    	
    	//--Affected by the camera--//
    	g2d.translate(-cam.pos.x, -cam.pos.y);
    	
		//------------DRAW HERE---------------------//
		g.dispose();
		bs.show();
	}
	
	public void loadImages(){
		biome_images = new BufferedImage[biome.getImg().getWidth()/32][biome.getImg().getHeight()/32];
		for (int i = 0; i < biome.getImg().getWidth()/32; i++) {
			for (int j = 0; j < biome.getImg().getHeight()/32; j++) {
				biome_images[i][j] = biome.getImg().getSubimage(i*32, j*32, 32, 32);
			}
		}
		
	}
	
	
	void updateTiles(Graphics2D g,int w0, int h0,int w, int h,double s){		
		
		for(int i = 0; i < chunk;i++){
			for(int j = 0; j < chunk;j++){
				int x1 = (int) (j*32*s);
				int y1 = (int) (i*32*s);
				if(x1 < w && y1 < h && x1 > w0 && y1 > h0){
					
					int id = tiles[i][j].getId();
					int y = (int) (tiles[i][j].getUp() * tiles[i][j].getDown());
  	    			int x = (int) (tiles[i][j].getLeft() * tiles[i][j].getRight());
  	    			int mx = (int) map(x1,0,chunk*32*mapArray.length,0,9);
  	    			int my = (int) map(y1,0,chunk*32*mapArray.length,0,9);
  	    			int b = (int) mapArray[mx][my];
  	    			//BiomeLayer
  	    			g.drawImage(biome_images[1][1+(b*2)], null, x1, y1);
  	    			//Detail Layer
					//g.drawImage(biome_images[x][y+(id*3)],null, x1, y1);
	  	    	}
	  	    }
		}
	}
  
    public void addTiles(){
  	  for(int i = 0; i < chunk; i++){
  	    for(int j = 0; j < chunk; j++){
  	      tiles[i][j] = new Tiles();
  	      tiles[i][j].setId((int) noiseDetail[i][j]);
  	      tiles[i][j].setElevation((int) noiseEl[i][j]);
  	    }
  	  }
  	}
  
    private float[][] generateNoise(int width, int height,int a, int b) {
	    new SimplexNoise(new Random().nextInt(10000));
	    float[][] noise = new float[width][height];
	    //Frequency = features. Higher = more features
	    float layerF = 0.003f;
	    //Weight = smoothness. Higher frequency = more smoothness
	    float weight = 0.5f;
	   
	    for(int i = 0; i < 3; i++) {
	        for(int x = 0; x < width; x++) {
              for(int y = 0; y < height; y++) { 	
                  noise[x][y] += (float) SimplexNoise.noise(x * layerF, y * layerF) * weight;
                  noise[x][y] = clamp(noise[x][y], -1.0f, 1.0f);
                  noise[x][y] = (float) Math.abs(Math.floor(map(noise[x][y],0,1,a,b)));
              }
	        }
	        layerF *= 3.5f;
	        weight *= 0.5f;
	    }
	   
	    return noise;
	}
	
    public void addProperties(){
		  for(int i = 1; i < chunk-1; i++){
		    for(int j = 1; j < chunk-1; j++){
		      //Checking if tiles next to itself are equal to itself
		      if(tiles[i][j].getId() == tiles[i-1][j].getId()) tiles[i][j].setUp(2);
		      else tiles[i][j].setUp(0);
		      
		      if(tiles[i][j].getId() == tiles[i+1][j].getId()) tiles[i][j].setDown(0.5f);
		      else tiles[i][j].setDown(1);
		      
		      if(tiles[i][j].getId() == tiles[i][j-1].getId()) tiles[i][j].setLeft(2);
		      else tiles[i][j].setLeft(0);
		      
		      if(tiles[i][j].getId() == tiles[i][j+1].getId()) tiles[i][j].setRight(0.5f);
		      else tiles[i][j].setRight(1);
		      
		      //----------------ELEVATION--------------------//
		      
		      if(tiles[i][j].getElevation() > tiles[i+1][j].getElevation()) 
		        tiles[i][j].setWall(1);
		      else tiles[i][j].setWall(0);
		      
		      if(j != 0 || tiles[i][j].getElevation() > tiles[i][j-1].getElevation()) 
		        tiles[i][j].setlCorner(10);
		      else tiles[i][j].setlCorner(0);
		      
		      if(j != chunk-1 || tiles[i][j].getElevation() > tiles[i][j+1].getElevation()) 
		        tiles[i][j].setrCorner(100);
		      else tiles[i][j].setrCorner(0);
		      
		      if(j != 0 || tiles[i][j].getElevation() > tiles[i][j-1].getElevation()) 
		        tiles[i][j].setlEdge(1000);
		      else tiles[i][j].setlEdge(0);
		      
		      if(j != chunk-1 || tiles[i][j].getElevation() > tiles[i][j+1].getElevation()) 
		        tiles[i][j].setrEdge(10000);
		      else tiles[i][j].setrEdge(0);
		      
		      if(i != 0 || tiles[i][j].getElevation() > tiles[i-1][j].getElevation()) 
		        tiles[i][j].setEnd(100000);
		      else tiles[i][j].setEnd(0);
		      
		      
		      //EDGES
		      if(i-1 == 0){ 
		       if(tiles[i-1][j].getId() == tiles[i+1][j].getId()) tiles[i-1][j].setDown(0.5f);
		       else tiles[i-1][j].setDown(1);
		       
		       if(tiles[i-1][j].getId() == tiles[i][j-1].getId()) tiles[i-1][j].setLeft(2);
		       else tiles[i-1][j].setLeft(0);
		      
		       if(tiles[i-1][j].getId() == tiles[i][j+1].getId()) tiles[i-1][j].setRight(0.5f);
		       else tiles[i-1][j].setRight(1);
		       
		       tiles[i-1][j].setUp(2);
		      
		      }
		      if(i+1 == chunk-1){
		        if(tiles[i+1][j].getId() == tiles[i-1][j].getId()) tiles[i+1][j].setUp(2);
		        else tiles[i+1][j].setUp(0);
		        
		        if(tiles[i+1][j].getId() == tiles[i][j-1].getId()) tiles[i+1][j].setLeft(2);
		        else tiles[i+1][j].setLeft(0);
		      
		        if(tiles[i+1][j].getId() == tiles[i][j+1].getId()) tiles[i+1][j].setRight(0.5f);
		        else tiles[i+1][j].setRight(1);
		        
		        tiles[i+1][j].setDown(0.5f);
		      }
		      if(j-1 == 0){ 
		        if(tiles[i][j-1].getId() == tiles[i-1][j].getId()) tiles[i][j-1].setUp(2);
		        else tiles[i][j-1].setUp(0);
		      
		        if(tiles[i][j-1].getId() == tiles[i+1][j].getId()) tiles[i][j-1].setDown(0.5f);
		        else tiles[i][j-1].setDown(1);
		      
		        if(tiles[i][j-1].getId() == tiles[i][j+1].getId()) tiles[i][j-1].setRight(0.5f);
		        else tiles[i][j-1].setRight(1);
		        
		        tiles[i][j-1].setLeft(2);
		      }
		      if(j+1 == chunk-1){ 
		        
		        if(tiles[i][j+1].getId() == tiles[i-1][j].getId()) tiles[i][j+1].setUp(2);
		        else tiles[i][j+1].setUp(0);
		      
		        if(tiles[i][j+1].getId() == tiles[i+1][j].getId()) tiles[i][j+1].setDown(0.5f);
		        else tiles[i][j+1].setDown(1);
		      
		        if(tiles[i][j+1].getId() == tiles[i][j-1].getId()) tiles[i][j+1].setLeft(2);
		        else tiles[i][j+1].setLeft(0);
		        
		        tiles[i][j+1].setRight(0.5f);
		      
		      }
		      
		    }
		  }
		}		
	
    public float clamp(float x, float min, float max){
  	if(x < min)			return min;
  	else if(x > max) 	return max;
  	else 				return x;
  }
  
    public float map(float x, float a, float b, float c, float d){
  	return (x-a)/(b-a)*(d-c);
  }
	
	
	public static void main(String args[]){
		new Window(800,800, "Map generator", new Game());
	}


}
