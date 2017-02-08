package mapGenerator;

import java.util.Vector;
	
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
