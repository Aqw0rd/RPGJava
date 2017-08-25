package Framework;

import Maths.Maths;
import Maths.Vector2f;
import Maths.Vector2i;
import Object.Player;
import UI.ActionBar;
import UI.ActionSlots;
import UI.HealthBar;
import UI.InvSlots;
import UI.Inventory;
import UI.ManaBar;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;

import mapGenerator.Handler;
import mapGenerator.ObjectId;
import mapGenerator.SimplexNoise;
import mapGenerator.TileSets;
import mapGenerator.Tiles;
import mapGenerator.UIHandler;
import mapGenerator.UIid;
import mapGenerator.Window;

public class Game
        extends Canvas
        implements Runnable {
    //-----------------------------Global vars--------------------------//

    int[][] noiseEl;
    int[][] mapArray;
    int[][] treeArray;

    Map map;
    BufferedImage[][] biome_images;
    BufferedImage[] baseImages;
    BufferedImage[][] transitions;
    BufferedImage[][] elevation;
    BufferedImage[] rocks;
    BufferedImage[] grass;
    BufferedImage[] trees;
    Camera cam;


    TileSets treeTiles = new TileSets();
    TileSets rockTiles = new TileSets();
    TileSets surface = new TileSets();
    TileSets baseTiles = new TileSets();
    TileSets elTiles = new TileSets();
    TileSets biome = new TileSets();
    TileSets transitionTiles = new TileSets();
    TileSets grassTiles = new TileSets();

    int mapSize;
    int x;
    int y;

    Handler handler;
    UIHandler uiHandler;
    //-----------------------------------||----------------------------//

    public static int WIDTH;
    public static int HEIGHT;
    private static final long serialVersionUID = -2413771048344743376L;
    private boolean running = false;
    private Thread thread;

    public synchronized void start() {
        if (this.running) {
            return;
        }
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void setUp() {
        this.mapSize = 500;

        this.x = 0;
        this.y = 0;

        this.cam = new Camera();            //Camera object
        this.handler = new Handler();       //Handler object
        this.uiHandler = new UIHandler();   //UIHandler object
        this.handler.addObject(new Player(100.0F, 100.0F, ObjectId.Player));    //Adding the playerobject to the handler

        addKeyListener(new KeyInput(this.handler, this.uiHandler)); //Focusing keylistener to an object of KeyInput which tracks the handler and uihandler

        WIDTH = getWidth();
        HEIGHT = getHeight();

        this.uiHandler.addObject(new ActionBar(100.0F, HEIGHT - 60, 600.0F, 60.0F, true, UIid.ActionBar));      //Adding the ActionBar
        this.uiHandler.addObject(new HealthBar(495.0F, HEIGHT - 55, 200.0F, 23.0F, true, UIid.HealthBar));      //Adding the HealthBar
        this.uiHandler.addObject(new ManaBar(495.0F, HEIGHT - 28, 200.0F, 23.0F, true, UIid.ManaBar));          //Adding the ManaBar
        this.uiHandler.addObject(new Inventory(530.0F, HEIGHT - 235, 170.0F, 170.0F, false, UIid.Inventory));   //Adding the Inventory

        int i = 0;
        for (UIid id : UIid.actionSlots) {
            this.uiHandler.addObject(new ActionSlots(i * 55 + 105, HEIGHT - 55, 50.0F, 50.0F, true, id)); //Adding the Slots to the ActionBar
            i++;
        }
        //Adding all the inventory slots
        i = 0;
        for (UIid id : UIid.invSlots) {
            if (i < 3) {
                this.uiHandler.addObject(new InvSlots(i * 55 + 535, HEIGHT - 230, 50.0F, 50.0F, true, id));
            } else if (i < 6) {
                this.uiHandler.addObject(new InvSlots((i - 3) * 55 + 535, HEIGHT - 230 + 55, 50.0F, 50.0F, true, id));
            } else {
                this.uiHandler.addObject(new InvSlots((i - 6) * 55 + 535, HEIGHT - 230 + 110, 50.0F, 50.0F, true, id));
            }
            i++;
        }

        //loading all the tilesets/images
        this.surface.setImg("src/resources/surface.png");
        this.biome.setImg("src/resources/biome.png");
        this.baseTiles.setImg("src/resources/basetiles.png");
        this.transitionTiles.setImg("src/resources/transitions.png");
        this.elTiles.setImg("src/resources/elevation.png");
        this.rockTiles.setImg("src/resources/rocks.png");
        this.grassTiles.setImg("src/resources/grass.png");
        this.treeTiles.setImg("src/resources/tree.png");

        this.mapArray = generateNoise(this.mapSize, this.mapSize, 0, 2, 0.003F);    //Array of noise, which inself is the map

        this.noiseEl = generateNoise(this.mapSize, this.mapSize, 0, 5, 3.0E-4F);    //Not used
        this.treeArray = generateNoise(this.mapSize, this.mapSize, 0, 1, 0.003F);   //Not used

        this.map = new Map(this.mapSize, this.mapArray);
        BufferedImage img = new BufferedImage(this.mapSize * 2, this.mapSize * 2, 2);
        File f = null;

        //creating an image of pixels based on the mapArray, (Basically a minimap)
        for (int x = 0; x < this.mapSize * 2; x += 2) {
            for (int y = 0; y < this.mapSize * 2; y += 2) {
                int a = 255;
                int r = 0;
                int g = 0;
                int b = 0;
                switch (this.map.tile_ground[(x / 2)][(y / 2)].getId()) {
                    case 0:
                        r = 66;
                        g = 119;
                        b = 244;
                        break;
                    case 1:
                        r = 80;
                        g = 244;
                        b = 66;
                        break;
                    case 2:
                        r = 99;
                        g = 63;
                        b = 25;
                        break;
                    case 3:
                        r = 231;
                        g = 189;
                        b = 88;
                        break;
                    case 4:
                        r = 91;
                        g = 91;
                        b = 91;
                        break;
                    case 5:
                        r = 220;
                        g = 153;
                        b = 75;
                }
                int p = a << 24 | r << 16 | g << 8 | b;
                img.setRGB(x, y, p);
                img.setRGB(x + 1, y, p);
                img.setRGB(x, y + 1, p);
                img.setRGB(x + 1, y + 1, p);
            }
        }

        try {
            f = new File(".\\Output.png");
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        loadImages();
    }

    public void run() {
        setUp();
        requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0D;
        double ns = 1.0E9D / amountOfTicks;
        double delta = 0.0D;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0D) {
                tick();
                updates++;
                delta -= 1.0D;
                render();
                frames++;
            }
            if (System.currentTimeMillis() - timer > 1000L) {
                timer += 1000L;
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                System.out.println("X: " + this.x + "Y: " + this.y);
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     *  Handling the gametick, and calling
     *  the tick functions of objects that need to be updated
     */
    private void tick() {
        for (int i = 0; i < this.handler.object.size(); i++) {
            GameObject temp = (GameObject) this.handler.object.get(i);
            if (temp.getId() == ObjectId.Player) {
                this.cam.tick(temp);
                for (int j = 0; j < this.uiHandler.object.size(); j++) {
                    UIObject uiTemp = (UIObject) this.uiHandler.object.get(j);
                    if (uiTemp.id == UIid.HealthBar) {
                        uiTemp.size.x = (uiTemp.fullSize.x * temp.hp / temp.maxHp);
                    }
                    if (uiTemp.id == UIid.ManaBar) {
                        uiTemp.size.x = (uiTemp.fullSize.x * temp.mana / temp.maxMana);
                    }
                }
            }
        }
        uiPos();
        this.uiHandler.tick();
        this.handler.tick();
    }

    /**
     * render function
     */
    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3); //Triplebuffering
            return;
        }
        Graphics g = bs.getDrawGraphics();  //Graphics object
        Graphics2D g2d = (Graphics2D) g;    //converting graphics to Graphics2D
        //------------DRAW HERE---------------------//
        g2d.translate(this.cam.pos.x, this.cam.pos.y);
        //--------------AFFECTED BY CAMERA-----------------//
        updateTiles(g2d, -this.cam.pos.x, -this.cam.pos.y, getWidth() + (-this.cam.pos.x),
                getHeight() + (-this.cam.pos.y));

        this.uiHandler.render(g);
        this.handler.render(g);
        //--------------AFFECTED BY CAMERA-----------------//
        g2d.translate(-this.cam.pos.x, -this.cam.pos.y);
        //--------------DRAW HERE-----------------//
        g.dispose();
        bs.show();
    }

    /**
     * Loading all the images into memory
     */
    public void loadImages() {
        this.baseImages = new BufferedImage[this.baseTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.baseTiles.getImg().getWidth() / 32; i++) {
            this.baseImages[i] = this.baseTiles.getImg().getSubimage(i * 32, 0, 32, 32);
        }
        this.transitions =
                new BufferedImage[this.transitionTiles.getImg().getWidth() / 32][this.transitionTiles.getImg().getHeight() / 32];
        for (int i = 0; i < this.transitionTiles.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.transitionTiles.getImg().getHeight() / 32; j++) {
                this.transitions[i][j] = this.transitionTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
        this.elevation = new BufferedImage[this.elTiles.getImg().getWidth() / 32][this.elTiles.getImg().getHeight() / 32];
        for (int i = 0; i < this.elTiles.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.elTiles.getImg().getHeight() / 32; j++) {
                this.elevation[i][j] = this.elTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
        this.rocks = new BufferedImage[this.rockTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.rockTiles.getImg().getWidth() / 32; i++) {
            this.rocks[i] = this.rockTiles.getImg().getSubimage(i * 32, 0, 32, 32);
        }
        this.grass = new BufferedImage[this.grassTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.grassTiles.getImg().getWidth() / 32; i++) {
            this.grass[i] = this.grassTiles.getImg().getSubimage(i * 32, 0, 32, 32);
        }
        this.trees = new BufferedImage[this.treeTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.treeTiles.getImg().getWidth() / 32; i++) {
            this.trees[i] = this.treeTiles.getImg().getSubimage(i * 32, 0, 32, 32);
        }
    }

    /**
     * Drawing all the tiles
     * @param g Graphics2D object
     * @param x0 Camera 0 x position
     * @param y0 Camera 0 y position
     * @param x  Camera x position
     * @param y  Camera y position
    */
    public void updateTiles(Graphics2D g, int x0, int y0, int x, int y) {
        int a = (int) Math.floor(new Maths().map(x0, -getWidth(), this.mapSize * 32, -(getWidth()/32), this.mapSize));
        int b = (int) Math.floor(new Maths().map(x, -getWidth(), this.mapSize * 32, -(getWidth()/32), this.mapSize));

        int c = (int) Math.floor(new Maths().map(y0,  -getHeight(), this.mapSize * 32, -(getHeight()/32), this.mapSize));
        int d = (int) Math.floor(new Maths().map(y,  -getHeight(), this.mapSize * 32, -(getHeight()/32), this.mapSize));
        a--;
        b++;
        c--;
        d++;
        int xpos, ypos;
        for (int i = a; i < b; i++) {
            for (int j = c; j < d; j++) {
                xpos = i;
                ypos = j;
                if (i < 0) {
                    xpos = mapSize+i;
                }
                if (i >= mapSize) {
                    xpos = i - mapSize;
                }
                if (j < 0) {
                    ypos = mapSize+j;
                }
                if (j >= mapSize) {
                    ypos= j - mapSize;
                }
                g.drawImage(this.baseImages[this.map.tile_ground[xpos][ypos].getId()], null, i * 32, j * 32);
                if (this.map.details[xpos][ypos].getId() == 1) {
                    g.drawImage(this.grass[0], null, i * 32, j * 32);
                }
                for (int t = 0; t < this.map.transArray.size(); t++) {
                    if (((Tiles[][]) this.map.transArray.get(Integer.valueOf(t)))[xpos][ypos].getBinary() > 0) {
                        g.drawImage(this.transitions[(((Tiles[][]) this.map.transArray.get(Integer.valueOf(t)))[xpos][ypos].getBinary() - 1)][t], null, i * 32, j * 32);
                    }
                    if (((Tiles[][]) this.map.cornerArray.get(Integer.valueOf(t)))[xpos][ypos].getBinary() > 0) {
                        g.drawImage(this.transitions[(((Tiles[][]) this.map.cornerArray.get(Integer.valueOf(t)))[xpos][ypos].getBinary() - 1)][t], null, i * 32, j * 32);
                    }
                }
                if ((this.map.details[xpos][ypos].getId() >= 2) && (this.map.details[xpos][ypos].getId() <= 4)) {
                    g.drawImage(this.rocks[(this.map.details[xpos][ypos].getId() - 1)], null, i * 32, j * 32);
                }
                if (this.map.details[xpos][ypos].getId() == 5) {
                    g.drawImage(this.trees[1], null, i * 32, j * 32);
                    g.drawImage(this.trees[0], null, (i - 1) * 32, j * 32);
                    g.drawImage(this.trees[2], null, (i - 1) * 32, (j - 1) * 32);
                    g.drawImage(this.trees[3], null, i * 32, (j - 1) * 32);
                    g.drawImage(this.trees[4], null, (i - 1) * 32, (j - 2) * 32);
                    g.drawImage(this.trees[5], null, i * 32, (j - 2) * 32);
                }
            }
        }
    }

    /**
     * Function for updating the UI position
     */
    public void uiPos() {
        for (int i = 0; i < this.uiHandler.object.size(); i++) {
            UIObject tempObject = (UIObject) this.uiHandler.object.get(i);

            tempObject.pos.x = (tempObject.abspos.x - this.cam.pos.x);
            tempObject.pos.y = (tempObject.abspos.y - this.cam.pos.y);
        }
    }

    /**
     * Function to generate noise
     * @param width desired map width
     * @param height desired map height
     * @param a min value
     * @param b max value
     * @param layerF Frequency = features. Higher = more features
     * @return a 2d integer array
     */
    private int[][] generateNoise(int width, int height, int a, int b, float layerF) {
        new SimplexNoise(new Random().nextInt(10000));
        int[][] noise = new int[width][height];
        //Weight = smoothness. Higher frequency = more smoothness
        float weight = 1.0F;
        for (int i = 0; i < 3; i++) {
            float ns = 0.0F;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    ns += (float) SimplexNoise.noise(x * layerF, y * layerF) * weight;
                    ns = new Maths().clamp(ns, -1.0F, 1.0F);
                    noise[x][y] = ((int) Math.abs(Math.floor(new Maths().map(ns, -1.0F, 1.0F, a, b))));
                }
            }
            layerF *= 3.5F;
            weight *= 0.5F;
        }
        return noise;
    }

    public static void main(String[] args) {
        new Window(800, 660, "Map generator", new Game());
    }
}