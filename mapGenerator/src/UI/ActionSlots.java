package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import framework.UIObject;
import mapGenerator.SpellID;
import mapGenerator.UIid;

public class ActionSlots extends UIObject{

	private SpellID spellID = null;
	
	public ActionSlots(float x, float y, float w, float h, boolean visible, UIid id) {
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
		
	}
	
	public SpellID getSpellID(){
		return this.spellID;
	}
	
	public void setSpellID(SpellID spellID){
		this.spellID = spellID;
	}

}
