package Framework;

import Maths.Vector2f;
import Maths.Vector2i;
import Maths.Vector3i;

public class Camera {
    Vector3i pos;

    public Camera() {
        this.pos = new Vector3i(0, 0, 2.0f);
    }

    public void tick(GameObject player, int w, int h, int wMax, int hMax) {
        int width = (int)(w/this.pos.z);
        int height = (int)(h/this.pos.z);
        this.pos.x = (int) -(player.pos.x - width / 2);
        this.pos.y = (int) -(player.pos.y - height / 2);
        if(this.pos.x>0) this.pos.x = 0;
        if(this.pos.y>0) this.pos.y = 0;
        if(Math.abs(this.pos.x)>wMax-width){
            this.pos.x = -(wMax-width);
        }
        if(Math.abs(this.pos.y)>hMax-height){
            this.pos.y = -(hMax-height);
        }
    }
}
