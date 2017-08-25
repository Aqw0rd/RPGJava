package UI;

import Maths.Vector2f;
import Framework.UIObject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import mapGenerator.UIid;

public class InvSlots
        extends UIObject {
    public InvSlots(float x, float y, float w, float h, boolean visible, UIid id) {
        super(x, y, w, h, visible, id);
    }

    public void render(Graphics g) {
        g.setColor(new Color(104, 104, 104));
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.size.x, (int) this.size.y);
    }

    public void tick(LinkedList<UIObject> object) {
        for (UIObject o : object) {
            if (o.getId() == UIid.Inventory) {
                this.visible = o.isVisible();
            }
        }
    }
}
