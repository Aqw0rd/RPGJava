package framework;

import Maths.Vector2i;
	
public class Camera {
	Vector2i pos;
	public Camera(){
		pos = new Vector2i(0,0);
	}
	public void tick(GameObject player){
		pos.x = (int) -(player.pos.x - Game.WIDTH/2);
		pos.y = (int) -(player.pos.y - Game.HEIGHT/2);
		if(pos.x > 0) pos.x = 0;
		if(pos.y > 0) pos.y = 0;
		
	}
}
