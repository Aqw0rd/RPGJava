package Framework;

import Maths.Vector2f;

import java.awt.Graphics;
import java.util.LinkedList;

import mapGenerator.UIid;

public abstract class UIObject {
    protected Vector2f abspos = new Vector2f(0.0F, 0.0F);
    protected Vector2f pos = new Vector2f(0.0F, 0.0F);
    protected Vector2f size = new Vector2f(0.0F, 0.0F);
    protected boolean visible;
    protected UIid id;
    protected Vector2f fullSize = new Vector2f(0.0F, 0.0F);

    public UIObject(float x, float y, float w, float h, boolean visible, UIid id) {
        this.abspos.x = x;
        this.abspos.y = y;
        this.size.x = w;
        this.size.y = h;
        this.visible = visible;
        this.id = id;
        this.fullSize.x = w;
        this.fullSize.y = h;
    }

    public abstract void render(Graphics paramGraphics);

    public abstract void tick(LinkedList<UIObject> paramLinkedList);

    public Vector2f getAbsPos() {
        return this.abspos;
    }

    public void setAbsPos(Vector2f pos) {
        this.abspos = pos;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public UIid getId() {
        return this.id;
    }
}
