package Object;

import Framework.GameObject;
import Maths.Maths;
import mapGenerator.ObjectId;
import mapGenerator.TileSets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

public class Bat extends GameObject {
    private enum State {
        Idle,
        Chase,
        Attack
    }

    private State _state;
    private int width = 32;
    private int height = 32;
    private double playerRange;
    private float targetX;
    private float targetY;
    public Bat(float x, float y, ObjectId id, String imgPath){
        super(x, y, id);
        this.hp = 100.0F;
        this.maxHp = 100.0F;
        this.mana = 100.0F;
        this.orientation = 0;
        this.animation = 0;
        this.animationTime = 0;
        this.speed = 3.0f;
        this.tileSets = new TileSets();
        this.tileSets.setImg(imgPath);
        _state = State.Idle;
        this.img = new BufferedImage[this.tileSets.getImg().getWidth() / 32][this.tileSets.getImg().getHeight() / 32];
        for (int i = 0; i < this.tileSets.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.tileSets.getImg().getHeight() / 32; j++) {
                this.img[i][j] = this.tileSets.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img[this.animation][this.orientation],null, (int)this.pos.x, (int)this.pos.y);
    }

    public void tick(LinkedList<GameObject> object, double gametick) {
        for(GameObject o: object){
            if(o.getId() == ObjectId.Player){
                targetX = o.getPos().x;
                targetY = o.getPos().y;
                playerRange = Math.sqrt(Math.pow((targetX - this.pos.x), 2) +
                        Math.pow((targetY - this.pos.y), 2));
            }
        }

        switch(_state){
            case Idle:
                idle();
                break;
            case Chase:
                chase();
                break;
        }

        this.pos.x += this.vel.x;
        this.pos.y += this.vel.y;

    }

    public Rectangle getBounds()        { return new Rectangle(    (int) this.pos.x, (int) this.pos.y, width, height); }
    public Rectangle getBoundsTop()     { return new Rectangle( (int) this.pos.x + 8, (int) this.pos.y, width, height/2); }
    public Rectangle getBoundsBottom()  { return new Rectangle( (int) this.pos.x + 8, (int) this.pos.y + height/2, width/2, height/2); }
    public Rectangle getBoundsLeft()    { return new Rectangle(    (int) this.pos.x, (int) this.pos.y + 8, 8, height - 16); }
    public Rectangle getBoundsRight()   { return new Rectangle( (int) this.pos.x + this.width - 8, (int) this.pos.y + 8, 8, height - 16); }

    private void idle(){
        if(playerRange>300){
            _state = State.Chase;
        }
    }

    private void chase(){
        float tX = targetX - this.pos.x;
        float tY = targetY - this.pos.y;
        float rotation = (float) (Math.atan2(tY, tX) * 180 / Math.PI);
        this.vel.x = this.speed * (90 - Math.abs(rotation)) / 90;
        this.vel.y = (rotation < 0) ? Math.abs(this.vel.x) - this.speed : this.speed - Math.abs(this.vel.x);

        if(playerRange < 80) {
            this.vel.x = 0;
            this.vel.y = 0;
            _state = State.Idle; //Will change to State.Attack once attack function is created
        }
    }


    public float getMaxHp() {
        return this.maxHp;
    }
    public float getHp() {
        return this.maxHp;
    }


}



