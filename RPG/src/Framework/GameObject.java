package Framework;

import Maths.Vector2f;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

import mapGenerator.ObjectId;
import mapGenerator.TileSets;

public abstract class GameObject {
    protected Vector2f pos = new Vector2f(0.0F, 0.0F);
    protected ObjectId id;
    protected Vector2f vel = new Vector2f(0.0F, 0.0F);
    protected float hp;
    protected float mana;
    protected float maxHp;
    protected float maxMana;
    protected int orientation;
    protected int animation;
    protected float speed;
    protected int animationTime;
    protected float animationSpeed;
    protected TileSets tileSets;
    protected BufferedImage[][] img;
    protected boolean solid = false;
    protected boolean running = false;

    /**
     * GameObject constructor
     * @param x
     * @param y
     * @param id
     */
    public GameObject(float x, float y, ObjectId id) {
        this.pos.x = x;
        this.pos.y = y;
        this.id = id;
    }

    /**
     *
     * @param Graphics
     */
    public abstract void render(Graphics Graphics);

    /**
     *
     * @param paramLinkedList
     */
    public abstract void tick(LinkedList<GameObject> LinkedList, double gametick);


    /**
     *
     * @return
     */
    public abstract Rectangle getBounds();

    /**
     *
     * @return
     */
    public Vector2f getPos() {
        return this.pos;
    }

    /**
     *
     * @param pos
     */
    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    /**
     *
     * @return
     */
    public Vector2f getVel() {
        return this.vel;
    }

    /**
     *
     * @param vel
     */
    public void setVel(Vector2f vel) {
        this.vel = vel;
    }

    /**
     *
     * @return
     */
    public ObjectId getId() {
        return this.id;
    }

    /**
     *
     * @return
     */
    public float getMaxHp() {
        return this.maxHp;
    }

    /**
     *
     * @return
     */
    public float getMaxMana() {
        return this.maxMana;
    }

    /**
     *
     * @return
     */
    public float getHp() {
        return this.maxHp;
    }

    /**
     *
     * @return
     */
    public float getMana() {
        return this.mana;
    }

    public boolean isSolid() {
        return this.solid;
    }

}
