package framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import mapGenerator.Handler;
import mapGenerator.ObjectId;

public class KeyInput extends KeyAdapter {
	Handler handler;
	
	public KeyInput(Handler handler){
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.Player){
				if(key == KeyEvent.VK_W) tempObject.vel.y = -5; 
				if(key == KeyEvent.VK_S) tempObject.vel.y = 5; 
				if(key == KeyEvent.VK_A) tempObject.vel.x = -5; 
				if(key == KeyEvent.VK_D) tempObject.vel.x = 5; 
			}
		}
		
		if(key == KeyEvent.VK_ESCAPE){
			System.exit(1);
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
			}
		}
	}
}
