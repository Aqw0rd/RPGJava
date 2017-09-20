package Object.Rocks;

import Framework.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import Maths.Maths;
import mapGenerator.ObjectId;
import mapGenerator.TileSets;

public class Stone2
        extends GameObject {
    public Stone2(float x, float y, ObjectId id, String imgPath) {
        super(x, y, id, imgPath);
        this.tileSets = new TileSets();
        this.tileSets.setImg(imgPath);
        this.img = new BufferedImage[this.tileSets.getImg().getWidth() / 32][this.tileSets.getImg().getHeight() / 32];
        for (int i = 0; i < this.tileSets.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.tileSets.getImg().getHeight() / 32; j++) {
                //this.img[i][j] = this.tileSets.getImg().getSubimage(i * 64, j * 64, 64, 64);
                this.img[i][j] = this.tileSets.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img[0][0],null, (int)this.pos.x, (int)this.pos.y);
        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }

    public void tick(LinkedList<GameObject> object, double gametick) {
    }

    public Rectangle getBounds() {
        return new Rectangle((int) this.pos.x, (int) this.pos.y, 32, 32);
    }
}