package mapGenerator;

import Framework.UIObject;

import java.awt.Graphics;
import java.util.LinkedList;

public class UIHandler {
    public LinkedList<UIObject> object = new LinkedList<UIObject>();
    private UIObject tempObject;

    public void tick(double gametick) {
        for (int i = 0; i < this.object.size(); i++) {
            this.tempObject = ((UIObject) this.object.get(i));

            this.tempObject.tick(this.object, gametick);
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < this.object.size(); i++) {
            this.tempObject = ((UIObject) this.object.get(i));
            if (this.tempObject.isVisible()) {
                this.tempObject.render(g);
            }
        }
    }

    public void addObject(UIObject object) {
        this.object.add(object);
    }

    public void removeObject(UIObject object) {
        this.object.remove(object);
    }
}
