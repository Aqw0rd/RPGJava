package mapGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import Maths.Maths;
import Maths.Vector2i;
import framework.Map;

public class Path {
	public HashSet<ArrayList<Vector2i>> path;
	HashSet<PathTile> openList;
	HashSet<PathTile> notCheckedList;
	HashSet<Vector2i> closedList;
	public Path(Map map){
		path = new  HashSet<ArrayList<Vector2i>>();
		int[][] region = createPlace(map.dimension);
		int[][] crossroads = createCrossroads(region);
		int[][] obstacles = obstacleMap(map);
		notCheckedList = new HashSet<PathTile>();
		openList = new HashSet<PathTile>();
		closedList = new HashSet<Vector2i>();
		ArrayList<PathTile> points = createPoints(region,obstacles);
		ArrayList<PathTile> crossroadPoints = createCrossroadsPoints(crossroads,obstacles, region);
		/*for(int i = 0; i < points.size(); i++){
			int x =  (int) new Maths().map(points.get(i).getPos().x,0,map.dimension,0,map.dimension/100);
			int y =  (int) new Maths().map(points.get(i).getPos().y,0,map.dimension,0,map.dimension/100);
			for(int j = 0; j < crossroadPoints.size(); j++){
				int x1 =  (int) new Maths().map(crossroadPoints.get(j).getPos().x,0,map.dimension,0,map.dimension/100);
				int y1 =  (int) new Maths().map(crossroadPoints.get(j).getPos().y,0,map.dimension,0,map.dimension/100);
				if(x1 == x && y1 == y) {
					path.add(astar(points.get(i),crossroadPoints.get(j),map.dimension,obstacles));
					break;
				}
			}
			
			
		}
		for(int i = 1; i < crossroadPoints.size(); i++){
			path.add(astar(crossroadPoints.get(i-1),crossroadPoints.get(i),map.dimension,obstacles));
		}*/
		for(int i = 0; i < points.size()-1; i++){
			int tempH = 0;
 			int temp = 0;
			int y = 0;
			for(int j = 0; j < points.size(); j++){
				try{
				if(!points.get(i).getInlet().equals(points.get(j)) && i !=j){				
					 temp =Math.abs(points.get(i).getPos().x - points.get(j).getPos().x) + Math.abs(points.get(i).getPos().y - points.get(j).getPos().y);
					 if(j == 0 || temp < tempH || tempH == 0){
						 tempH =temp;
						 y = j;
					 }
				}
				}catch(NullPointerException e){
					temp =Math.abs(points.get(i).getPos().x - points.get(j).getPos().x) + Math.abs(points.get(i).getPos().y - points.get(j).getPos().y);
					 if(j == 0 || temp < tempH || tempH == 0){
						 tempH =temp;
						 y = j;
					 }
				}
			}
			points.get(y).setInlet(points.get(i));
			path.add(astar(points.get(i),points.get(y),map.dimension,obstacles));
		}
	}
	
	public ArrayList<PathTile> createPoints(int[][] region, int[][] obstacles){
		ArrayList<PathTile> points = new ArrayList<PathTile>();
		 for(int i = 0; i < region.length; i++){
		        for(int j = 0; j < region.length; j++){
		        	if(region[i][j] == 1){
			        	int x = new Maths().randomInt(j*20,((j+1)*20)-1);
			        	int y = new Maths().randomInt(i*20,((i+1)*20)-1);
			        	while(obstacles[x][y] == 1){
			        		 x = new Maths().randomInt(j*20,((j+1)*20)-1);
			        		 y = new Maths().randomInt(i*20,((i+1)*20)-1);
			        	}
			        	PathTile point = new PathTile();
			        	point.setPos(new Vector2i(x,y));
			        	points.add(point);
		        	}
		        }      
		 }
		
		
		return points;
	}
	
	public ArrayList<PathTile> createCrossroadsPoints(int[][] crossroads, int[][] obstacles, int[][] region){
		ArrayList<PathTile> points = new ArrayList<PathTile>();
		 for(int i = 0; i < crossroads.length; i++){
			 String reg = "";
		        for(int j = 0; j < crossroads.length; j++){
		        	reg += crossroads[i][j];
		        	if(crossroads[i][j] == 1){
			        	int x1 =  (int) new Maths().map(i,0,crossroads.length,0,region.length);
			            int y1 =  (int) new Maths().map(j,0,crossroads.length,0,region.length);
			        	int x = new Maths().randomInt(y1*20,((y1+1)*20)-1);
			        	int y = new Maths().randomInt(x1*20,((x1+1)*20)-1);
			        	while(obstacles[x][y] == 1){
			        		 x = new Maths().randomInt(y1*20,((y1+1)*20)-1);
			        		 y = new Maths().randomInt(x1*20,((x1+1)*20)-1);
			        	}
			        	PathTile point = new PathTile();
			        	point.setPos(new Vector2i(x,y));
			        	points.add(point);
		        	}
		        	
		        }  
		        System.out.println(reg);
		 }
		
		
		return points;
	}
	
	public int[][] createCrossroads(int[][] region){
		int[][] crossroads = new int[region.length/5][region.length/5];
		for(int i = 0; i < crossroads.length; i++){
	        for(int j = 0; j < crossroads.length; j++){
	        	crossroads[i][j] = 0;
	        }
		}
	    for(int i = 0; i < region.length; i++){
	        for(int j = 0; j < region.length; j++){
	            if(region[i][j] == 1){
		            int x =  (int) new Maths().map(i,0,region.length,0,crossroads.length);
		            int y =  (int) new Maths().map(j,0,region.length,0,crossroads.length);
		            crossroads[x][y] = 1;
	            }
	        }
	    }
	    return crossroads;
	}
	
	public int[][] createPlace(int mapSize){
		int[][] region = new int[mapSize/20][mapSize/20];

	    for(int i = 0; i < region.length; i++){
	        for(int j = 0; j < region.length; j++){
	            region[i][j] = (new Random().nextInt(100) > 98) ? 1 : 0;
	        }
	    }
	    return region;
	}

	public int[][] obstacleMap(Map map){
		int obstacles [][] = new int [map.tile_ground.length][map.tile_ground.length];
		 for(int i = 0; i < obstacles.length; i++){
		        for(int j = 0; j < obstacles.length; j++){
		        	/*if(	map.tile_ground[i][j].getId() == 0 || 
		        		(i>0 ? map.tile_ground[i-1][j].getId() == 0 : false) ||
		        		(i<obstacles.length -1 ? map.tile_ground[i+1][j].getId() == 0 : false) ||
        				(j>0 ? map.tile_ground[i][j-1].getId() == 0 : false) ||
		        		(j<obstacles.length -1 ? map.tile_ground[i][j+1].getId() == 0 : false)) obstacles[i][j] = 1;
		        	else{*/
		        	obstacles[i][j] = 0;
		        	//}
		        }   
		 }
		
		
		return obstacles;
	}
	
	public ArrayList<Vector2i> astar(PathTile a, PathTile b, int mapSize,int[][] obstacles){
		ArrayList<PathTile> used = new ArrayList<PathTile>();
		boolean finished = false;
		notCheckedList.clear();
		openList.clear();
		closedList.clear();
		notCheckedList.add(a);
		openList.add(a);
		used.add(a);
		used.add(b);
		PathTile current = a;
		while(!current.equals(b)){
			int tempF = 0;
			boolean first = true;
			Iterator<PathTile> iterator = openList.iterator();
			if(notCheckedList.size() > 0) iterator = notCheckedList.iterator(); 
			ArrayList<PathTile> deleteList = new ArrayList<PathTile>();
			while(iterator.hasNext()){
				PathTile temp = (PathTile) iterator.next();
				if(notCheckedList.size() > 0) deleteList.add(temp);
				if(first || temp.getF() < tempF){
					first = false;
					tempF = temp.getF();
					current = temp;
				}
			}
			for(PathTile p: deleteList){
				notCheckedList.remove(p);
			}
			
			int x = current.getPos().x;
			int y = current.getPos().y;
			int xend = x;
			int yend = y;
			int xstart = x;
			int ystart = y;
			if(x == 0) xend = mapSize;
			if(y == 0) yend = mapSize;
			if(x == mapSize-1) xstart = -1;
			if(y == mapSize-1) ystart = -1;
				//if(x != 0 && x != mapSize-1 && y != 0 && y != mapSize-1) {
			
		         	if(obstacles[xstart+1][y] == 0 && !closedList.contains(new Vector2i(xstart+1,y))){
		        	 	PathTile adjacent = new PathTile();   
		        		adjacent.setPos(new Vector2i(x+1,y));
		                adj(adjacent,10,current,b, used);
		         	}

		            if(obstacles[xend-1][y] == 0 && !closedList.contains(new Vector2i(xend-1,y))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x-1,y));
		                adj(adjacent,10,current,b, used);
		            }  

		            if(obstacles[x][ystart+1] == 0 && !closedList.contains(new Vector2i(x,ystart+1))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x,y+1));
		                adj(adjacent,10,current,b,used);
		            }    

		            if(obstacles[x][yend-1] == 0 && !closedList.contains(new Vector2i(x,yend-1))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x,y-1));
		                adj(adjacent,10,current,b, used);
		            }
		            
		            //Diagonal
		            /*if(obstacles[x+1][y+1] == 0 && !closedList.contains(new Vector2i(x+1,y))){
		        	 	PathTile adjacent = new PathTile();   
		        		adjacent.setPos(new Vector2i(x+1,y+1));
		                adj(adjacent,14,current,b, used);
		         	}

		            if(obstacles[x-1][y-1] == 0 && !closedList.contains(new Vector2i(x-1,y))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x-1,y-1));
		                adj(adjacent,14,current,b, used);
		            }  

		            if(obstacles[x-1][y+1] == 0 && !closedList.contains(new Vector2i(x,y+1))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x-1,y+1));
		                adj(adjacent,14,current,b,used);
		            }    

		            if(obstacles[x+1][y-1] == 0 && !closedList.contains(new Vector2i(x,y-1))){
		            	PathTile adjacent = new PathTile();
		            	adjacent.setPos(new Vector2i(x+1,y-1));
		                adj(adjacent,14,current,b, used);
		            }*/
		            
					
				//}
				
			closedList.add(current.getPos());
			iterator = openList.iterator();
			while(iterator.hasNext()){
				PathTile temp = (PathTile) iterator.next();
				if(temp.getH() == 0 && !current.equals(a)){
					finished = true;
					b.setParent(temp);
					break;
				}
					
			}
			openList.remove(current);
			if(finished) break;
			
		}
		ArrayList<Vector2i> temppath = new ArrayList<Vector2i>();
		while(!current.getParent().equals(a)){
			temppath.add(current.getParent().getPos());
			current = current.getParent();
		}
		for(PathTile s: used){
			s.setParent(null);
		}
		return temppath;		
	}
	
	public void adj(PathTile a, int g, PathTile current, PathTile b, ArrayList<PathTile> used){
		if(!openList.contains(a)){
			a.setParent(current);
			a.setG(g + a.getParent().getG());
			int H = Math.abs(a.getPos().x - b.getPos().x) + Math.abs(a.getPos().y - b.getPos().y);
			H = H*10;
			a.setH(H);
			a.setF(a.getG() + a.getH());
			openList.add(a);
			notCheckedList.add(a);
			used.add(a);
		}else{
			if(a.getG() > current.getG() + g){
				a.setParent(current);
				a.setG(current.getG()+g);
				a.setF(a.getG() + a.getH());
			}
		}
	}
}
