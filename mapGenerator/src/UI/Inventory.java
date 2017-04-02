package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.UIObject;
import mapGenerator.UIid;

public class Inventory extends UIObject {

	public Inventory(float x, float y, float w, float h, boolean visible, UIid id) {
		super(x, y, w, h, visible, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(new Color(51,51,51));
		g.fillRect((int)pos.x, (int)pos.y, (int)size.x, (int)size.y);
	}

	@Override
	public void tick(LinkedList<UIObject> object) {
		// TODO Auto-generated method stub
		
	}

}