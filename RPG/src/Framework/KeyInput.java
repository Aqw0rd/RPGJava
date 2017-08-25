package Framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import mapGenerator.Handler;
import mapGenerator.ObjectId;
import mapGenerator.UIHandler;
import mapGenerator.UIid;

public class KeyInput
        extends KeyAdapter {
    Handler handler;
    UIHandler uiHandler;

    public KeyInput(Handler handler, UIHandler uiHandler) {
        this.handler = handler;
        this.uiHandler = uiHandler;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < this.handler.object.size(); i++) {
            GameObject tempObject = (GameObject) this.handler.object.get(i);
            if (tempObject.getId() == ObjectId.Player) {
                if (key == 87) {
                    tempObject.vel.y = -5;
                }
                if (key == 83) {
                    tempObject.vel.y = 5;
                }
                if (key == 65) {

                    tempObject.vel.x = -5;

                }
                if (key == 68) {
                    tempObject.vel.x = 5;
                }
                if (key == 32) {
                    tempObject.hp -= 1.0F;
                }
            }
        }
        if (key == 27) {
            System.exit(1);
        }
        if (key == 73) {
            for (int j = 0; j < this.uiHandler.object.size(); j++) {
                UIObject ui = (UIObject) this.uiHandler.object.get(j);
                if (ui.id == UIid.Inventory) {
                    ui.visible = (!ui.visible);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < this.handler.object.size(); i++) {
            GameObject tempObject = (GameObject) this.handler.object.get(i);
            if (tempObject.getId() == ObjectId.Player) {
                if (key == 87) {
                    tempObject.vel.y = 0.0F;
                }
                if (key == 83) {
                    tempObject.vel.y = 0.0F;
                }
                if (key == 65) {
                    tempObject.vel.x = 0.0F;
                }
                if (key == 68) {
                    tempObject.vel.x = 0.0F;
                }
            }
        }
    }
}
