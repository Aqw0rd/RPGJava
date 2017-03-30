package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;


import framework.UIObject;
import mapGenerator.UIid;

public class HealthBar extends UIObject{
	
	protected int outline = 1;
	
	public HealthBar(float x, float y, float w, float h, boolean visible, UIid id) {
		super(x, y, w, h, visible, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		//Back
		g.setColor(new Color(0));
		g.fillRect((int)pos.x-outline, (int)pos.y-outline, (int)fullSize.x+outline*2, (int)fullSize.y+outline*2);
		
		//Front
		g.setColor(new Color(66,244,104));
		g.fillRect((int)pos.x, (int)pos.y, (int)size.x, (int)size.y);
		
	}

	@Override
	public void tick(LinkedList<UIObject> object) {
		// TODO Auto-generated method stub
		
	}

}
