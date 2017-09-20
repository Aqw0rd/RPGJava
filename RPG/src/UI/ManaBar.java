package UI;

import Maths.Vector2f;
import Framework.UIObject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import mapGenerator.UIid;

public class ManaBar
        extends UIObject {
    private int outline = 1;

    public ManaBar(float x, float y, float w, float h, boolean visible, UIid id) {
        super(x, y, w, h, visible, id);
    }

    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.size.x, (int) this.size.y);

        g.fillRect((int) this.pos.x - this.outline, (int) this.pos.y - this.outline, (int) this.fullSize.x + this.outline * 2, (int) this.fullSize.y + this.outline * 2);

        g.setColor(new Color(66, 134, 244));
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.size.x, (int) this.size.y);
    }

    public void tick(LinkedList<UIObject> objec, double tick) {
    }
}
