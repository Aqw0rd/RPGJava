package mapGenerator;

public class Tiles {
	  private int id=0;
	  private int up=0;
	  private float down=0;
	  private int left=0;
	  private float right=0;
	  private int elevation=0;
	  private int wall=0;
	  private int lCorner=0;
	  private int rCorner=0;
	  private int lEdge=0;
	  private int rEdge=0;
	  private int end=0;
	  private int biome = 0;
	  
	  public int getId() {
	    return id;
	  }
	  public void setId(int id) {
	    this.id = id;
	  }
	  public int getUp() {
	    return up;
	  }
	  public void setUp(int up) {
	    this.up = up;
	  }
	  public float getDown() {
	    return down;
	  }
	  public void setDown(float down) {
	    this.down = down;
	  }
	  public int getLeft() {
	    return left;
	  }
	  public void setLeft(int left) {
	    this.left = left;
	  }
	  public float getRight() {
	    return right;
	  }
	  public void setRight(float right) {
	    this.right = right;
	  }
	  public int getElevation() {
	    return elevation;
	  }
	  public void setElevation(int elevation) {
	    this.elevation = elevation;
	  }
	  public int getWall() {
	    return wall;
	  }
	  public void setWall(int wall) {
	    this.wall = wall;
	  }
	  public int getlCorner() {
	    return lCorner;
	  }
	  public void setlCorner(int lCorner) {
	    this.lCorner = lCorner;
	  }
	  public int getrCorner() {
	    return rCorner;
	  }
	  public void setrCorner(int rCorner) {
	    this.rCorner = rCorner;
	  }
	  public int getlEdge() {
	    return lEdge;
	  }
	  public void setlEdge(int lEdge) {
	    this.lEdge = lEdge;
	  }
	  public int getrEdge() {
	    return rEdge;
	  }
	  public void setrEdge(int rEdge) {
	    this.rEdge = rEdge;
	  }
	  public int getEnd() {
	    return end;
	  }
	  public void setEnd(int end) {
	    this.end = end;
	  }
	}