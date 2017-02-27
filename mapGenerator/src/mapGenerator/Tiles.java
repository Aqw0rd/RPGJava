package mapGenerator;

public class Tiles {
	private int id = 0;
	private int up = 0;
	private int down = 0;
	private int left = 0;
	private int right = 0;
	private int elevation = 0;
	private int elUp = 0;
	private int elDown = 0;
	private int elLeft = 0;
	private int elRight = 0;
	private boolean elTransition = false;

	private int wall = 0;
	private int lCorner = 0;
	private int rCorner = 0;
	private int lEdge = 0;
	private int rEdge = 0;
	private int end = 0;
	private boolean transition = false;
	private int transitionId;
	private boolean corner = false;
	private boolean edge = false;

	public boolean isElTransition() {
		return elTransition;
	}

	public void setElTransition(boolean elTransition) {
		this.elTransition = elTransition;
	}

	public int getElUp() {
		return elUp;
	}

	public void setElUp(int elUp) {
		this.elUp = elUp;
	}

	public int getElDown() {
		return elDown;
	}

	public void setElDown(int elDown) {
		this.elDown = elDown;
	}

	public int getElLeft() {
		return elLeft;
	}

	public void setElLeft(int elLeft) {
		this.elLeft = elLeft;
	}

	public int getElRight() {
		return elRight;
	}

	public void setElRight(int elRight) {
		this.elRight = elRight;
	}

	public boolean isEdge() {
		return edge;
	}

	public void setEdge(boolean edge) {
		this.edge = edge;
	}

	public boolean isCorner() {
		return corner;
	}

	public void setCorner(boolean corner) {
		this.corner = corner;
	}

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

	public boolean isTransition() {
		return transition;
	}

	public void setTransition(boolean transition) {
		this.transition = transition;
	}

	public int getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(int transitionId) {
		this.transitionId = transitionId;
	}
}