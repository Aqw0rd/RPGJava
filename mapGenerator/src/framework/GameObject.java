package framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import Maths.Vector2f;
import mapGenerator.ObjectId;

public abstract class GameObject {
	protected Vector2f pos = new Vector2f(0,0);
	protected ObjectId id;
	protected Vector2f vel = new Vector2f(0,0);
	
	public GameObject(float x, float y, ObjectId id){
		this.pos.x = x;
		this.pos.y = y;
		this.id = id;
	}
	
	public abstract void render(Graphics g);
	public abstract void tick(LinkedList<GameObject> object);
	public abstract Rectangle getBounds();
	
	public  Vector2f getPos(){
		return pos;
	}
	public  void setPos(Vector2f pos){
		this.pos = pos;
	}
	
	public  Vector2f getVel(){
		return vel;
	}
	public  void setVel(Vector2f vel){
		this.vel = vel;
	}
	
	public  ObjectId getId(){
		return id;
	}
}
