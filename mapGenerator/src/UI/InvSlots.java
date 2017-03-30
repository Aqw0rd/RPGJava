package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.UIObject;
import mapGenerator.UIid;

public class InvSlots extends UIObject {

	public InvSlots(float x, float y, float w, float h, boolean visible, UIid id) {
		super(x, y, w, h, visible, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(new Color(104,104,104));
		g.fillRect((int)pos.x, (int)pos.y, (int)size.x, (int)size.y);
	}

	@Override
	public void tick(LinkedList<UIObject> object) {
		// TODO Auto-generated method stub
		for(UIObject o: object)
			if(o.getId() == UIid.Inventory) this.visible = o.isVisible();
	
	}

}
