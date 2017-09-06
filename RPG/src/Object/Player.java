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
    private float height = 32.0F;
    private float width = 32.0F;

    public Player(float x, float y, ObjectId id, String imgPath) {
        super(x, y, id, imgPath);
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
                this.img[i][j] = new Maths().getScaledImage(this.tileSets.getImg().getSubimage(i * 64, j * 64, 64, 64),42,42);
            }
        }
    }

    public void render(Graphics g) {
        //g.setColor(Color.gray);
        //g.fillRect((int) this.pos.x, (int) this.pos.y, (int) this.width, (int) this.height);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img[this.animation][this.orientation],null, (int)this.pos.x, (int)this.pos.y);
    }

    public void tick(LinkedList<GameObject> object, double gametick) {
        float[] vel = new float[] {this.vel.x, this.vel.y};
        /*switch (Arrays.toString(vel)){
            case "[0.0, 0.0]":
                this.animation = 1;
                break;
            case "[5.0, 0.0]":
                this.orientation = 3;
                break;
            case "[-5.0, 0.0]":
                this.orientation = 2;
                break;
            case "[5.0, 5.0]":
                this.orientation = 0;
                break;
            case "[5.0, -5.0]":
                this.orientation = 1;
                break;
            case "[0.0, 5.0]":
                this.orientation = 0;
                break;
            case "[0.0, -5.0]":
                this.orientation = 1;
                break;
            case "[-5.0, 5.0]":
                this.orientation = 0;
                break;
            case "[-5.0, -5.0]":
                this.orientation = 1;
                break;
        }*/
        //if(Math.abs(this.vel.x) == this.speed && Math.abs(this.vel.y) > 0) this.vel.x =this.vel.x/(float)Math.sqrt(2);
        //if(Math.abs(this.vel.y) == this.speed && Math.abs(this.vel.x) > 0) this.vel.y =this.vel.y/(float)Math.sqrt(2);

        this.pos.x += this.vel.x;
        this.pos.y += this.vel.y;

        if(Math.abs(this.vel.x) > 0 || Math.abs(this.vel.y) > 0 ) this.animationTime ++;
        else {
            this.animation = 0;
            this.animationTime = 0;
        }
        if(this.animationTime>=10) {
            this.animation ++;
            if(this.animation>=this.tileSets.getImg().getWidth() / 64) this.animation = 0;
            this.animationTime = 0;
        }
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


