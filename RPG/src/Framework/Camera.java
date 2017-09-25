package Framework;

import Maths.Vector2f;
import Maths.Vector2i;

public class Camera {
    Vector2i pos;

    public Camera() {
        this.pos = new Vector2i(0, 0);
    }

    public void tick(GameObject player, int w, int h, int wMax, int hMax) {
        this.pos.x = (int) -(player.pos.x - w / 2);
        this.pos.y = (int) -(player.pos.y - h / 2);
        if(this.pos.x>0) this.pos.x = 0;
        if(this.pos.y>0) this.pos.y = 0;
        if(Math.abs(this.pos.x)>wMax-w) this.pos.x = -(wMax-w);
        if(Math.abs(this.pos.y)>hMax-h) this.pos.y = -(hMax-h);
    }
}
