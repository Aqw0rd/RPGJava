package Framework;

import Maths.Vector2f;
import Maths.Vector2i;

public class Camera {
    Vector2i pos;

    public Camera() {
        this.pos = new Vector2i(0, 0);
    }

    public void tick(GameObject player, int w, int h) {
        this.pos.x = ((int) -(player.pos.x - w / 2));
        this.pos.y = ((int) -(player.pos.y - h / 2));
    }
}
