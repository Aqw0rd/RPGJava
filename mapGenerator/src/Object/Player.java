package Object;

import java.awt.Graphics;

import Maths.Vector2f;
import framework.GameObject;
import mapGenerator.ObjectId;

public class Player extends GameObject {

	public Player(float x, float y, ObjectId id) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2f getPos() {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public void setPos(Vector2f pos) {
		// TODO Auto-generated method stub
		this.pos = pos;
	}

	@Override
	public Vector2f getVel() {
		// TODO Auto-generated method stub
		return vel;
	}

	@Override
	public void setVel(Vector2f vel) {
		// TODO Auto-generated method stub
		this.vel = vel;
	}

	@Override
	public ObjectId getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
