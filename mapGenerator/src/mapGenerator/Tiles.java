package mapGenerator;

public class Tiles {
	private int id = 0;
	private int type = 0;
	private int up = 0;
	private int down = 0;
	private int left = 0;
	private int right = 0;
	/*private boolean transition = false;
	private int transitionId;
	private boolean corner = false;
	private int cornerId = 0;*/
	private boolean edge = false;
	/*private int elevation = 0;
	private int elUp = 0;
	private int elDown = 0;
	private int elLeft = 0;
	private int elRight = 0;
	private boolean elTransition = false;
	private int cornerType = 0;
	private int wall = 0;
	private int lCorner = 0;
	private int rCorner = 0;
	private int lEdge = 0;
	private int rEdge = 0;
	private int end = 0;
	private boolean elEdge = false;*/
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUp() {
		return up;
	}
	public void setUp(int up) {
		this.up = up;
	}
	public int getDown() {
		return down;
	}
	public void setDown(int down) {
		this.down = down;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public boolean isEdge() {
		return edge;
	}
	public void setEdge(boolean edge) {
		this.edge = edge;
	}
	
	
	
}