package UI;

import Maths.Vector2f;
import Framework.UIObject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import mapGenerator.UIid;

public class Inventory
        extends UIObject {
    public Inventory(float x, float y, float w, float h, boolean visible, UIid id) {
        super(x, y, w, h, visible, id);
    }

    public void render(Graphics g) {
        g.setColor(new Color(51, 51, 51));
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.size.x, (int) this.size.y);
    }

    public void tick(LinkedList<UIObject> object) {
    }
}
