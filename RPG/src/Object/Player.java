package Object;

import Maths.Vector2f;
import Framework.GameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import mapGenerator.ObjectId;

public class Player
        extends GameObject {
    private float height = 32.0F;
    private float width = 32.0F;

    public Player(float x, float y, ObjectId id) {
        super(x, y, id);

        this.hp = 100.0F;
        this.maxHp = 100.0F;
        this.mana = 100.0F;
        this.maxMana = 100.0F;
    }

    public void render(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.width, (int) this.height);
    }

    public void tick(LinkedList<GameObject> object) {
        this.pos.x += this.vel.x;
        this.pos.y += this.vel.y;

        //If the player crosses the 0 or max position, recalculate the position
        if(this.pos.x < 0) this.pos.x = 500*32 + this.pos.x;
        if(this.pos.y < 0 ) this.pos.y = 500*32 + this.pos.y;
        if(this.pos.x >= 500*32) this.pos.x = this.pos.x - 500*32;
        if(this.pos.y >= 500*32) this.pos.y = this.pos.y - 500*32;

    }

    public Rectangle getBounds() {
        return new Rectangle((int) this.pos.x, (int) this.pos.y, 32, 32);
    }

    public float getMaxHp() {
        return this.maxHp;
    }

    public float getMaxMana() {
        return this.maxMana;
    }

    public float getHp() {
        return this.maxHp;
    }

    public float getMana() {
        return this.mana;
    }
}
