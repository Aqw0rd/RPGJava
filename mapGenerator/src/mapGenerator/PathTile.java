package mapGenerator;

import Maths.Vector2i;

public class PathTile {
	private int G,H,F;
	private boolean start,end;
	private Vector2i pos;
	private PathTile parent;
	private PathTile inlet;
	
	
	public PathTile getInlet() {
		return inlet;
	}
	public void setInlet(PathTile inlet) {
		this.inlet = inlet;
	}
	
	public PathTile getParent() {
		return parent;
	}
	public void setParent(PathTile parent) {
		this.parent = parent;
	}
	public Vector2i getPos() {
		return pos;
	}
	public void setPos(Vector2i pos) {
		this.pos = pos;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G = g;
	}
	public int getH() {
		return H;
	}
	public void setH(int h) {
		H = h;
	}
	public int getF() {
		return F;
	}
	public void setF(int f) {
		F = f;
	}
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
}
