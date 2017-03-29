package framework;

import java.awt.Graphics;
import java.util.LinkedList;

import Maths.Vector2f;
import mapGenerator.UIid;

public abstract class UIObject {
	
	protected Vector2f abspos = new Vector2f(0,0);
	protected Vector2f pos = new Vector2f(0,0);
	protected Vector2f size = new Vector2f(0,0);
	protected boolean visible;
	protected UIid id;
	protected Vector2f fullSize = new Vector2f(0,0);
	
	public UIObject(float x, float y,float w, float h, boolean visible, UIid id){
		this.abspos.x = x;
		this.abspos.y = y;
		this.size.x = w;
		this.size.y = h;
		this.visible = visible;
		this.id = id;
	}
	
	public abstract void render(Graphics g);
	public abstract void tick(LinkedList<UIObject> object);
	
	public  Vector2f getAbsPos(){
		return abspos;
	}
	public  void setAbsPos(Vector2f pos){
		this.abspos = pos;
	}
}
