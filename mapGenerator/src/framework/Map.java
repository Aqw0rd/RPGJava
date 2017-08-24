package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import Maths.Maths;
import Maths.Vector2i;
import mapGenerator.Path;
import mapGenerator.Tiles;

public class Map {
	public Tiles[][] tile_ground;
	public Tiles[][] tile_elevation;
	public Tiles[][] details;
	public Tiles[][] corners;
	public Tiles[][] transition;
	public HashMap<Integer, Tiles[][]> transArray;
	public HashMap<Integer, Tiles[][]> cornerArray;
	public int dimension;
	public Path path;
	public Map(int mapSize, int[][] mapArray){
		dimension = mapSize;
		tile_ground = new Tiles[mapSize][mapSize];
		tile_elevation = new Tiles[mapSize][mapSize];
		details = new Tiles[mapSize][mapSize];
		corners = new Tiles[mapSize][mapSize];
		transition = new Tiles[mapSize][mapSize];
		transArray = new HashMap<Integer, Tiles[][]>();
		cornerArray = new HashMap<Integer, Tiles[][]>();
		for(int i = 0; i < 10; i++){
			transArray.put(i, new Tiles[mapSize][mapSize]);
			cornerArray.put(i, new Tiles[mapSize][mapSize]);
			for (int j = 0; j < transArray.get(i).length; j++) {
				for (int j2 = 0; j2 < transArray.get(i).length; j2++) {
					transArray.get(i)[j][j2] = new Tiles();
					transArray.get(i)[j][j2].setId(-1);
					cornerArray.get(i)[j][j2] = new Tiles();
					cornerArray.get(i)[j][j2].setId(-1);
				}
			}
		}
		
		addTiles(mapSize, mapArray);
		addDetails(mapSize);
		path = new Path(this);
		
		System.out.println("transArray: " + transArray.size());
		Iterator iterator = path.path.iterator();
		while(iterator.hasNext()){
			@SuppressWarnings("unchecked")
			ArrayList<Vector2i> temp = (ArrayList<Vector2i>) iterator.next();
			for(int i = 0; i < temp.size(); i++){
				int x = temp.get(i).x;
				int y = temp.get(i).y;
				if(tile_ground[x][y].getId() == 0) tile_ground[x][y].setId(5);
				else tile_ground[x][y].setId(2);
				if(this.details[x][y].getId() == 1) details[x][y].setId(0);
				
			}
		}
		properties(mapSize,false);
		
	}
	
	public void addTiles(int mapSize, int[][] mapArray) {
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
		
	for (int i = 0; i < mapSize; i++) {
		for (int j = 0; j < mapSize; j++) {
			int xend = i;
			int yend = j;
			int xstart = i;
			int ystart = j;
			if(i == 0) xend = mapSize;
			if(j == 0) yend = mapSize;
			if(i == mapSize-1) xstart = -1;
			if(j == mapSize-1)ystart = -1;
			tile_ground[i][j].setL(tile_ground[xend-1][j]);
			tile_ground[i][j].setR(tile_ground[xstart+1][j]);
			tile_ground[i][j].setU(tile_ground[i][yend-1]);
			tile_ground[i][j].setD(tile_ground[i][ystart+1]);
			
			tile_ground[i][j].setTl(tile_ground[xend-1][yend-1]);
			tile_ground[i][j].setTr(tile_ground[xstart+1][yend-1]);
			tile_ground[i][j].setBl(tile_ground[xend-1][ystart+1]);
			tile_ground[i][j].setBr(tile_ground[xstart+1][ystart+1]);
		}
	}
	}
	
	public void checkSelf(int mapSize){
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				// Checking if tiles next to itself are equal to itself
				try{
					if(tile_ground[i][j].getId() < tile_ground[i][j].getL().getId()){
						int id = (int) new Maths().map(tile_ground[i][j].getL().getId(), 1, 6, 0, 10);
						transArray.get(id)[i][j].addBinary(0b1);
						tile_ground[i][j].addBinary(0b1);
					}
				}catch(NullPointerException e){
					//System.out.println(e);
				}
				try{
					if(tile_ground[i][j].getId() < tile_ground[i][j].getU().getId()){
						int id = (int) new Maths().map(tile_ground[i][j].getU().getId(), 1, 6, 0, 10);
						transArray.get(id)[i][j].addBinary(0b10);
						tile_ground[i][j].addBinary(0b10);
					}
				}catch(NullPointerException e){
					//System.out.println(e);
				}
				try{
					if(tile_ground[i][j].getId() < tile_ground[i][j].getR().getId()){
						int id = (int) new Maths().map(tile_ground[i][j].getR().getId(), 1, 6, 0,10);
						transArray.get(id)[i][j].addBinary(0b100);
						tile_ground[i][j].addBinary(0b100);
					}
				}catch(NullPointerException e){
					//System.out.println(e);
				}
				try{
					if(tile_ground[i][j].getId() < tile_ground[i][j].getD().getId()){
						int id = (int) new Maths().map(tile_ground[i][j].getD().getId(), 1, 6, 0, 10);
						transArray.get(id)[i][j].addBinary(0b1000);
						tile_ground[i][j].addBinary(0b1000);
					}
				}catch(NullPointerException e){
					//System.out.println(e);
				}
			}
		}
	}

	public void checkCorners(int mapSize){
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				int id = tile_ground[i][j].getId();
				
				Integer u = (tile_ground[i][j].getU() !=null ? tile_ground[i][j].getU().getId() : null);
				Integer d = (tile_ground[i][j].getD() !=null ? tile_ground[i][j].getD().getId() : null);
				Integer l = (tile_ground[i][j].getL() !=null ? tile_ground[i][j].getL().getId() : null);
				Integer r = (tile_ground[i][j].getR() !=null ? tile_ground[i][j].getR().getId() : null);
				
				Integer tl = (tile_ground[i][j].getTl() !=null ? tile_ground[i][j].getTl().getId() : null);
				Integer tr = (tile_ground[i][j].getTr() !=null ? tile_ground[i][j].getTr().getId() : null);
				Integer bl = (tile_ground[i][j].getBl() !=null ? tile_ground[i][j].getBl().getId() : null);
				Integer br = (tile_ground[i][j].getBr() !=null ? tile_ground[i][j].getBr().getId() : null);
				int xend = i;
				int yend = j;
				int xstart = i;
				int ystart = j;
				if(i == 0) xend = mapSize;
				if(j == 0) yend = mapSize;
				if(i == mapSize-1) xstart = -1;
				if(j == mapSize-1)ystart = -1;
				if(id > tl && tl == u && tl == l){
					id = (int) new Maths().map(id, 1, 6, 0, 10);
					cornerArray.get(id+1)[xend-1][yend-1].addBinary(0b100);
					tile_ground[i][j].addBinary(0b100);
					id = tile_ground[i][j].getId();
				}
				if(id > tr && tr == u && tr == r){
					id = (int) new Maths().map(id, 1, 6, 0, 10);
					cornerArray.get(id+1)[xstart+1][yend-1].addBinary(0b1000);
					tile_ground[i][j].addBinary(0b10);
					id = tile_ground[i][j].getId();
				}

				if(id > bl && bl == d && bl == l){
					id = (int) new Maths().map(id, 1, 6, 0, 10);
					cornerArray.get(id+1)[xend-1][ystart+1].addBinary(0b10);
					tile_ground[i][j].addBinary(0b1000);
					id = tile_ground[i][j].getId();
				}
				try{
				if(id > br && br == d && br == r){
					id = (int) new Maths().map(id, 1, 6, 0, 10);
					cornerArray.get(id+1)[xstart+1][ystart+1].addBinary(0b1);
					tile_ground[i][j].addBinary(0b100);
					id = tile_ground[i][j].getId();
				}
				}catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void properties(int mapSize, boolean elevation){				
		checkSelf(mapSize);
		checkCorners(mapSize);
	}
	
	public void addDetails(int mapSize){
		Random rg = new Random();
		int r = 0;
		for (int i = 1; i < mapSize-1; i++) {
			for (int j = 1; j < mapSize-1; j++) {
				if(tile_ground[i][j].getId() == 1){ //If tile is of a type we want to spawn stuff on, e.g 1 = default grass
					r = rg.nextInt(100)+1;
					
					// r > 99 = 1% chance of spawning a tree, and its not on an edge of some sort. 
					if(r > 99 && !tile_ground[i][j].isEdge()) details[i][j].setId(5); 
					else{
						r = rg.nextInt(6)+1;
						int g1 = (details[i-1][j].getId() == 1) ? 1 : 0; //g1 and g2 checks to see wether there are
						int g2 = (details[i][j-1].getId() == 1) ? 1 : 0; //grass nearby, if there is, increase the probability of spawn
						r += g2 + g1;	
						if(r > 5) details[i][j].setId(1); //Lowest probability is 16% spawnrate, highest is 50%
						else{
							details[i][j].setId(0); //probably not necesseray
							r = rg.nextInt(100)+1;
							if(r>99) details[i][j].setId(rg.nextInt(3)+2); //1% chance of spawning a rock
						}
					}
				}
			}
		}
		
	}


}

