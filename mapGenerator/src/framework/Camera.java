package framework;

import java.util.Vector;

import Maths.Vector2i;
	
public class Camera {
	Vector2i pos;
	public Camera(){
		pos = new Vector2i(0,0);
	}
	public void tick(int x, int y){
		pos.x = x;
		pos.y = y;
	}
}
