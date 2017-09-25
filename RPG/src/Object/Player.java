package Object;

import Maths.*;
import Framework.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;

import mapGenerator.ObjectId;
import mapGenerator.TileSets;

public class Player
        extends GameObject {
    private int width = 42;
    private int height = 42;
    private boolean idle = true;
    private boolean right = true;

    public Player(float x, float y, ObjectId id, String imgPath) {
        super(x, y, id);
        this.hp = 100.0F;
        this.maxHp = 100.0F;
        this.mana = 100.0F;
        this.maxMana = 100.0F;
        this.orientation = 0;
        this.animation = 0;
        this.animationTime = 0;
        this.speed = 3.0f;
        this.tileSets = new TileSets();
        this.tileSets.setImg(imgPath);
        this.img = new BufferedImage[this.tileSets.getImg().getWidth() / 64][this.tileSets.getImg().getHeight() / 64];
        for (int i = 0; i < this.tileSets.getImg().getWidth() / 64; i++) {
            for (int j = 0; j < this.tileSets.getImg().getHeight() / 64; j++) {
                //this.img[i][j] = this.tileSets.getImg().getSubimage(i * 64, j * 64, 64, 64);
                this.img[i][j] = new Maths().getScaledImage(this.tileSets.getImg().getSubimage(i * 64, j * 64, 64, 64),width,height);
            }
        }
    }

    public void render(Graphics g) {
        //g.setColor(Color.gray);
        //g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.width, (int) this.height);
        Graphics2D g2d = (Graphics2D) g;
        if(this.idle){
            if(right) g2d.drawImage(this.img[this.animation][0],null, (int)this.pos.x, (int)this.pos.y);
            else g2d.drawImage(this.img[this.animation][0], (int)this.pos.x + width, (int)this.pos.y, -width, height, null);
        }
        else{
            if(right) g2d.drawImage(this.img[this.animation][1],null, (int)this.pos.x, (int)this.pos.y);
            else g2d.drawImage(this.img[this.animation][1], (int)this.pos.x + width, (int)this.pos.y, -width, height, null);
        }
        g.setColor(Color.RED);
        g2d.draw(getBoundsTop());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsBottom());

    }

    public void tick(LinkedList<GameObject> object, double gametick) {
        Collision(object);
        float[] vel = new float[] {this.vel.x, this.vel.y};
        if(this.vel.x == 0 && this.vel.y == 0) this.idle = true;
        else this.idle = false;
        if(this.vel.x != 0) this.right = (this.vel.x>0);


        this.pos.x += this.vel.x;
        this.pos.y += this.vel.y;

        /*if(Math.abs(this.vel.x) > 0 || Math.abs(this.vel.y) > 0 ) this.animationTime ++;
        else {
            this.animation = 0;
            this.animationTime = 0;
        }*/
        if(this.animationTime>=6) {
            this.animation ++;
            if(this.animation>=this.tileSets.getImg().getWidth() / 64) this.animation = 0;
            this.animationTime = 0;
        }
        this.animationTime++;
        //If the player crosses the 0 or max position, recalculate the position
        if(this.pos.x < 0) this.pos.x = 500*32 + this.pos.x;
        if(this.pos.y < 0 ) this.pos.y = 500*32 + this.pos.y;
        if(this.pos.x >= 500*32) this.pos.x = this.pos.x - 500*32;
        if(this.pos.y >= 500*32) this.pos.y = this.pos.y - 500*32;

    }

    public void Collision(LinkedList<GameObject> object){
        for(GameObject o: object){
            if(!o.equals(this) && o.isSolid()){
                if(getBoundsTop().intersects(o.getBounds())){
                    this.pos.y = o.getPos().y + 34;
                    //this.vel.y = 0;
                }
                if(getBoundsBottom().intersects(o.getBounds())){
                    this.pos.y = o.getPos().y - height;
                    //this.vel.y = 0;
                }
                if(getBoundsRight().intersects(o.getBounds())){
                    this.pos.x = o.getPos().x - width;
                    //this.vel.x = 0;
                }
                if(getBoundsLeft().intersects(o.getBounds())){
                    this.pos.x = o.getPos().x + 34;
                    //this.vel.x = 0;
                }
            }
        }
    }

    public Rectangle getBounds()        { return new Rectangle( (int) this.pos.x, (int) this.pos.y, width, height); }
    public Rectangle getBoundsTop()     { return new Rectangle((int) this.pos.x + 8, (int) this.pos.y, width-16, height/2); }
    public Rectangle getBoundsBottom()  { return new Rectangle((int) this.pos.x + 8, (int) this.pos.y + height/2, width-16, height/2); }
    public Rectangle getBoundsLeft()    { return new Rectangle((int) this.pos.x, (int) this.pos.y + 8, 8, height - 16); }
    public Rectangle getBoundsRight()   { return new Rectangle((int) this.pos.x + this.width - 8, (int) this.pos.y + 8, 8, height - 16); }


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


