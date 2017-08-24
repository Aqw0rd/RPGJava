package framework;

import java.awt.Graphics;

import Maths.Vector2f;
import mapGenerator.ObjectId;

public abstract class GameObject {
	protected Vector2f pos;
	protected ObjectId id;
	protected Vector2f vel = new Vector2f(0,0);
	
	public GameObject(float x, float y, ObjectId id){
		this.pos.x = x;
		this.pos.y = y;
		this.id = id;
	}
	
	public abstract void render(Graphics g);
	
	public abstract Vector2f getPos();
	public abstract void setPos(Vector2f pos);
	
	public abstract Vector2f getVel();
	public abstract void setVel(Vector2f vel);
	
	public abstract ObjectId getId();
}
