package Object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import framework.GameObject;
import mapGenerator.ObjectId;

public class Player extends GameObject {
	
	private float width = 32, height =32;
	
	
	public Player(float x, float y, ObjectId id) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
		hp = 100; 	maxHp = 100;
		mana = 100; maxMana = 100;
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.gray);
		g.fillRect((int)pos.x, (int)pos.y, (int)width, (int)height);
	}

	@Override
	public void tick(LinkedList<GameObject> object) {
		// TODO Auto-generated method stub
		pos.x += vel.x;
		pos.y += vel.y;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle((int)pos.x,(int)pos.y,32,32);
	}
	
	public float getMaxHp(){
		return maxHp;
	}
	public float getMaxMana(){
		return maxMana;
	}
	public float getHp(){
		return maxHp;
	}
	public float getMana(){
		return mana;
	}

}
