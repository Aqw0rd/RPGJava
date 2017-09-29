package Object;

import Maths.Vector2i;

public class Segment {
    public Vector2i a;
    public Vector2i b;
    public Segment(int x, int y, int xa, int ya, int xb, int yb){
        a = new Vector2i (xa + x, ya + y);
        b = new Vector2i (xb + x, yb + y);
    }
}
