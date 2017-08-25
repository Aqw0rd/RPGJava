package UI;

import Maths.Vector2f;
import Framework.UIObject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import mapGenerator.SpellID;
import mapGenerator.UIid;

public class ActionSlots
        extends UIObject {
    private SpellID spellID = null;

    public ActionSlots(float x, float y, float w, float h, boolean visible, UIid id) {
        super(x, y, w, h, visible, id);
    }

    public void render(Graphics g) {
        g.setColor(new Color(104, 104, 104));
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.size.x, (int) this.size.y);
    }

    public void tick(LinkedList<UIObject> object) {
    }

    public SpellID getSpellID() {
        return this.spellID;
    }

    public void setSpellID(SpellID spellID) {
        this.spellID = spellID;
    }
}
