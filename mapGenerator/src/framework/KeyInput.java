package framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import mapGenerator.Handler;
import mapGenerator.ObjectId;
import mapGenerator.UIHandler;
import mapGenerator.UIid;

public class KeyInput extends KeyAdapter {
	Handler handler;
	UIHandler uiHandler;
	
	public KeyInput(Handler handler, UIHandler uiHandler){
		this.handler = handler;
		this.uiHandler = uiHandler;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player){
				if(key == KeyEvent.VK_W) tempObject.vel.y = -20; 
				if(key == KeyEvent.VK_S) tempObject.vel.y = 20; 
				if(key == KeyEvent.VK_A) tempObject.vel.x = -20; 
				if(key == KeyEvent.VK_D) tempObject.vel.x = 20; 
				if(key == KeyEvent.VK_SPACE) tempObject.hp -= 1;
			}
		}
		
		if(key == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}
		
		if(key == KeyEvent.VK_I){
			
			for(int j = 0; j < uiHandler.object.size(); j++){
				UIObject ui = uiHandler.object.get(j);
				if(ui.id == UIid.Inventory) ui.visible = (!ui.visible);
			}
		}
		
		
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player){
				if(key == KeyEvent.VK_W) tempObject.vel.y = 0; 
				if(key == KeyEvent.VK_S) tempObject.vel.y = 0; 
				if(key == KeyEvent.VK_A) tempObject.vel.x = 0; 
				if(key == KeyEvent.VK_D) tempObject.vel.x = 0; 
				//if(key == KeyEvent.VK_SPACE) tempObject.hp -=0 ;
			}
		}
	}
}
