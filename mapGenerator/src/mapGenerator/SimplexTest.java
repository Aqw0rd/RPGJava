package mapGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

public class SimplexTest extends JFrame implements KeyListener {
	//-----------------------------Global vars--------------------------//
	float[][] noise;
	float[][] noiseEl;
	Tiles[][] tiles;
	Camera cam;
	int chunk;
    TileSets surface = new TileSets();   
    //-----------------------------------||----------------------------//
    public SimplexTest(){
    	chunk = 30;
    	cam = new Camera();
    	tiles = new Tiles[chunk][chunk];
    	surface.setImg("src/surface.png");
    	String array = "";    	
    	noise = generateNoise(chunk,chunk);
    	noiseEl = generateNoise(chunk,chunk);
    	addTiles();
    	addProperties();
    	this.addKeyListener(this);
    	this.setVisible(true);
    	this.setSize(720,720);

        }

    public void paint(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
    	
    	g2d.translate(cam.pos.x, cam.pos.y);
    	//--Affected by the camera--//
    	g.clearRect(0, 0, this.getWidth(), this.getHeight());
    	updateTiles(g2d);
    	//--Affected by the camera--//
    	g2d.translate(-cam.pos.x, -cam.pos.y);
    	cam.pos.x--;
    	System.out.println(cam.pos.x);
    	
    }
    
    void updateTiles(Graphics2D g){
    	  for(int i = 0; i < chunk;i++){
    	    for(int j = 0; j < chunk;j++){
    	      if(tiles[i][j].getType()==1){
    	        int y = (int) (tiles[i][j].getUp() * tiles[i][j].getDown() * 32);
    	        int x = (int) (tiles[i][j].getLeft() * tiles[i][j].getRight() * 32);
    	        //image(surface.get(x,y,32,32),j*32,i*32);
    	        g.drawImage(surface.getImg().getSubimage(x,y,32,32), null, j*32,i*32);
    	      }
    	      else if(tiles[i][j].getType()==0){
    	        int y = (int) (tiles[i][j].getUp() * tiles[i][j].getDown() * 32);
    	        int x = (int) (tiles[i][j].getLeft() * tiles[i][j].getRight() * 32);
    	        //image(surface.get(x,(6*32)+y,32,32),j*32,i*32);
    	        g.drawImage(surface.getImg().getSubimage(x,(6*32)+y,32,32), null, j*32,i*32);
    	      }
    	      else if(tiles[i][j].getType()==2){
    	        //image(surface.get(5*32,8*32,32,32),j*32,i*32);
    	        g.drawImage(surface.getImg().getSubimage(5*32,8*32,32,32), null, j*32,i*32);
    	      }
    	    }
    	  }
    }
    
    public void addTiles(){
    	  for(int i = 0; i < chunk; i++){
    	    for(int j = 0; j < chunk; j++){
    	      tiles[i][j] = new Tiles();
    	      tiles[i][j].setType((int) noise[i][j]);
    	      tiles[i][j].setElevation((int) noiseEl[i][j]);
    	    }
    	  }
    	}
    
	private float[][] generateNoise(int width, int height) {
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
                    noise[x][y] = (float) Math.abs(Math.floor(map(noise[x][y],0,1,0,2)));
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
		      if(tiles[i][j].getType() == tiles[i-1][j].getType()) tiles[i][j].setUp(2);
		      else tiles[i][j].setUp(0);
		      
		      if(tiles[i][j].getType() == tiles[i+1][j].getType()) tiles[i][j].setDown(0.5f);
		      else tiles[i][j].setDown(1);
		      
		      if(tiles[i][j].getType() == tiles[i][j-1].getType()) tiles[i][j].setLeft(2);
		      else tiles[i][j].setLeft(0);
		      
		      if(tiles[i][j].getType() == tiles[i][j+1].getType()) tiles[i][j].setRight(0.5f);
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
		       if(tiles[i-1][j].getType() == tiles[i+1][j].getType()) tiles[i-1][j].setDown(0.5f);
		       else tiles[i-1][j].setDown(1);
		       
		       if(tiles[i-1][j].getType() == tiles[i][j-1].getType()) tiles[i-1][j].setLeft(2);
		       else tiles[i-1][j].setLeft(0);
		      
		       if(tiles[i-1][j].getType() == tiles[i][j+1].getType()) tiles[i-1][j].setRight(0.5f);
		       else tiles[i-1][j].setRight(1);
		       
		       tiles[i-1][j].setUp(2);
		      
		      }
		      if(i+1 == chunk-1){
		        if(tiles[i+1][j].getType() == tiles[i-1][j].getType()) tiles[i+1][j].setUp(2);
		        else tiles[i+1][j].setUp(0);
		        
		        if(tiles[i+1][j].getType() == tiles[i][j-1].getType()) tiles[i+1][j].setLeft(2);
		        else tiles[i+1][j].setLeft(0);
		      
		        if(tiles[i+1][j].getType() == tiles[i][j+1].getType()) tiles[i+1][j].setRight(0.5f);
		        else tiles[i+1][j].setRight(1);
		        
		        tiles[i+1][j].setDown(0.5f);
		      }
		      if(j-1 == 0){ 
		        if(tiles[i][j-1].getType() == tiles[i-1][j].getType()) tiles[i][j-1].setUp(2);
		        else tiles[i][j-1].setUp(0);
		      
		        if(tiles[i][j-1].getType() == tiles[i+1][j].getType()) tiles[i][j-1].setDown(0.5f);
		        else tiles[i][j-1].setDown(1);
		      
		        if(tiles[i][j-1].getType() == tiles[i][j+1].getType()) tiles[i][j-1].setRight(0.5f);
		        else tiles[i][j-1].setRight(1);
		        
		        tiles[i][j-1].setLeft(2);
		      }
		      if(j+1 == chunk-1){ 
		        
		        if(tiles[i][j+1].getType() == tiles[i-1][j].getType()) tiles[i][j+1].setUp(2);
		        else tiles[i][j+1].setUp(0);
		      
		        if(tiles[i][j+1].getType() == tiles[i+1][j].getType()) tiles[i][j+1].setDown(0.5f);
		        else tiles[i][j+1].setDown(1);
		      
		        if(tiles[i][j+1].getType() == tiles[i][j-1].getType()) tiles[i][j+1].setLeft(2);
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == 87){ //W
			cam.pos.y -=5;
		}
		if(e.getKeyCode() == 83){ //A
			cam.pos.x -=5;
		}
		if(e.getKeyCode() == 65){ //S
			cam.pos.y +=5;
		}
		if(e.getKeyCode() == 68){ //d
			cam.pos.x +=5;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		
	}
}

