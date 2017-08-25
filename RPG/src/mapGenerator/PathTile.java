package mapGenerator;

import Maths.Vector2i;

public class PathTile {
    private int G;
    private int H;
    private int F;
    private boolean start;
    private boolean end;
    private Vector2i pos;
    private PathTile parent;

    public PathTile getParent() {
        return this.parent;
    }

    public void setParent(PathTile parent) {
        this.parent = parent;
    }

    public Vector2i getPos() {
        return this.pos;
    }

    public void setPos(Vector2i pos) {
        this.pos = pos;
    }

    public int getG() {
        return this.G;
    }

    public void setG(int g) {
        this.G = g;
    }

    public int getH() {
        return this.H;
    }

    public void setH(int h) {
        this.H = h;
    }

    public int getF() {
        return this.F;
    }

    public void setF(int f) {
        this.F = f;
    }

    public boolean isStart() {
        return this.start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return this.end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
