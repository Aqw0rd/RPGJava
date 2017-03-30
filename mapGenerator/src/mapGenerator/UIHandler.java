package mapGenerator;

import java.awt.Graphics;
import java.util.LinkedList;

import framework.UIObject;

public class UIHandler {
	
	public LinkedList<UIObject> object = new LinkedList<UIObject>();
	private UIObject tempObject;
	
	public void tick(){
		for(int i = 0; i < object.size(); i++){
			tempObject = object.get(i);
			
			tempObject.tick(object);
		}

	}
	
	public void render(Graphics g){
		for(int i = 0; i < object.size(); i++){
			tempObject = object.get(i);
			if(tempObject.isVisible())
				tempObject.render(g);
		}
	}
	
	public void addObject(UIObject object){
		this.object.add(object);
	}
	
	public void removeObject(UIObject object){
		this.object.remove(object);
	}
}
