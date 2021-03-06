package mapGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileSets {
    private BufferedImage img;

    public BufferedImage getImg() {
        return this.img;
    }

    public void setImg(String path) {
        try {
            this.img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
