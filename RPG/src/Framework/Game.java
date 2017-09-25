package Framework;

import Maths.Maths;
import Maths.Vector2f;
import Maths.Vector2i;
import Object.Player;
import Object.Bat;
import Object.Rocks.Stone2;
import Object.Rocks.Stone3;
import Object.Rocks.Stone4;
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
    JSON intro;
    BufferedImage[][] biome_images;
    BufferedImage[] baseImages;
    BufferedImage[][] transitions;
    BufferedImage[][] elevation;
    BufferedImage[] rocks;
    BufferedImage[] grass;
    BufferedImage[] trees;
    BufferedImage[] water;
    BufferedImage[][] gameobjects;
    BufferedImage[][] house;
    BufferedImage[] path;


    Camera cam;

    TileSets waterTiles = new TileSets();
    TileSets gameObjectTiles = new TileSets();
    TileSets houseTiles = new TileSets();
    TileSets pathTiles = new TileSets();
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
        this.handler.addObject(new Player(550, 100.0F, ObjectId.Player, "src/resources/player.png"));    //Adding the playerobject to the handler
        this.handler.addObject(new Bat(150.0F, 100.0F, ObjectId.Bat, "src/resources/bat.png"));

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
        this.waterTiles.setImg("src/resources/wateranimation.png");
        this.surface.setImg("src/resources/surface.png");
        this.biome.setImg("src/resources/biome.png");
        this.baseTiles.setImg("src/resources/basetiles.png");
        this.transitionTiles.setImg("src/resources/transitions.png");
        this.elTiles.setImg("src/resources/elevation.png");
        this.rockTiles.setImg("src/resources/rocks.png");
        this.grassTiles.setImg("src/resources/grass.png");
        this.treeTiles.setImg("src/resources/tree.png");
        this.gameObjectTiles.setImg("src/resources/gameobjects.png");
        this.houseTiles.setImg("src/resources/house.png");
        this.pathTiles.setImg("src/resources/path.png");

        this.mapArray = generateNoise(this.mapSize, this.mapSize, 0, 2, 0.0009F);    //Array of noise, which inself is the map

        this.noiseEl = generateNoise(this.mapSize, this.mapSize, 0, 5, 3.0E-4F);    //Not used
        this.treeArray = generateNoise(this.mapSize, this.mapSize, 0, 1, 0.003F);   //Not used

        //this.map = new Map(this.mapSize, this.mapArray);
        this.intro = new JSON("src/Campaign/intro level.json");


        //creating an image of pixels based on the mapArray, (Basically a minimap)
        //createMiniMap(this.mapSize, this.mapSize, this.map.tile_ground);
        loadImages();


        //createGameObjects()
    }

    public void run() {
        setUp();
        requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1.0E9 / amountOfTicks;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1.0D) {
                tick(delta);
                updates++;
                delta -= 1.0D;
                render();
                frames++;
            }
            if (System.currentTimeMillis() - timer > 1000L) {
                timer += 1000L;
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                //System.out.println("X: " + this.x + "Y: " + this.y);
                //System.out.println("WIDTH: " + this.getWidth() + ", HEIGHT: " + this.getHeight());
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     *  Handling the gametick, and calling
     *  the tick functions of objects that need to be updated
     */
    private void tick(double gametick) {
        for (int i = 0; i < this.handler.object.size(); i++) {
            GameObject temp = this.handler.object.get(i);
            if (temp.getId() == ObjectId.Player) {
                this.cam.tick(temp, this.getWidth(), this.getHeight(), this.intro.width*32, this.intro.height*32);
                for (int j = 0; j < this.uiHandler.object.size(); j++) {
                    UIObject uiTemp =  this.uiHandler.object.get(j);
                    if (uiTemp.id == UIid.HealthBar) {
                        uiTemp.size.x = (uiTemp.fullSize.x * temp.hp / temp.maxHp);
                    }
                    if (uiTemp.id == UIid.ManaBar) {
                        uiTemp.size.x = (uiTemp.fullSize.x * temp.mana / temp.maxMana);
                    }
                }
            }
        }

        /*for (int i = 0; i < this.mapSize; i++) {
            for (int j = 0; j < this.mapSize; j++) {
                Tiles tile = this.map.tile_ground[i][j];
                if(tile.getId() == 0){
                    if(tile.getL().getId() == tile.getId()) {
                        if (tile.getAnimation() + 2 < tile.getL().getAnimation() || tile.getAnimation() >= 7) {
                            this.map.tile_ground[i][j].animate(gametick, this.water.length);
                        }
                    }else{
                        this.map.tile_ground[i][j].animate(gametick, this.water.length);
                    }
                }

            }
        }*/

        uiPos();
        this.uiHandler.tick(gametick);
        this.handler.tick(gametick);
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
        //updateTiles(g2d, -this.cam.pos.x, -this.cam.pos.y, getWidth() + (-this.cam.pos.x),
                //getHeight() + (-this.cam.pos.y));

        drawTiles(g2d, Math.abs(cam.pos.x), Math.abs(cam.pos.y),
                this.getWidth() + Math.abs(cam.pos.x), this.getHeight() + Math.abs(cam.pos.y));


        this.handler.render(g);
        this.uiHandler.render(g);
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
        this.water = new BufferedImage[this.waterTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.waterTiles.getImg().getWidth() / 32; i++) {
            this.water[i] = this.waterTiles.getImg().getSubimage(i * 32, 0, 32, 32);
        }
        this.gameobjects = new BufferedImage[this.gameObjectTiles.getImg().getWidth() / 32][this.gameObjectTiles.getImg().getHeight() / 32];
        for (int i = 0; i < this.gameObjectTiles.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.gameObjectTiles.getImg().getHeight() / 32; j++) {
                this.gameobjects[i][j] = this.gameObjectTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
        this.house = new BufferedImage[this.houseTiles.getImg().getWidth() / 32][this.houseTiles.getImg().getHeight() / 32];
        for (int i = 0; i < this.houseTiles.getImg().getWidth() / 32; i++) {
            for (int j = 0; j < this.houseTiles.getImg().getHeight() / 32; j++) {
                this.house[i][j] = this.houseTiles.getImg().getSubimage(i * 32, j * 32, 32, 32);
            }
        }
        this.path = new BufferedImage[this.pathTiles.getImg().getWidth() / 32];
        for (int i = 0; i < this.pathTiles.getImg().getWidth() / 32; i++) {
            this.path[i] = this.pathTiles.getImg().getSubimage(i * 32, 0, 32, 32);
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
                if(this.map.tile_ground[xpos][ypos].getId() == 0)
                    g.drawImage(this.water[this.map.tile_ground[xpos][ypos].getAnimation()], null, i * 32, j * 32);
                else g.drawImage(this.baseImages[this.map.tile_ground[xpos][ypos].getId()], null, i * 32, j * 32);
                if (this.map.details[xpos][ypos].getId() == 1) {
                    g.drawImage(this.grass[0], null, i * 32, j * 32);
                }
                for (int t = 0; t < this.map.transArray.size(); t++) {
                    if ((this.map.transArray.get(t))[xpos][ypos].getBinary() > 0) {
                        g.drawImage(this.transitions[(((Tiles[][]) this.map.transArray.get(t))[xpos][ypos].getBinary() - 1)][t], null, i * 32, j * 32);
                    }
                    if ((this.map.cornerArray.get(t))[xpos][ypos].getBinary() > 0) {
                        g.drawImage(this.transitions[(((Tiles[][]) this.map.cornerArray.get(t))[xpos][ypos].getBinary() - 1)][t], null, i * 32, j * 32);
                    }
                }
               /* if ((this.map.details[xpos][ypos].getId() >= 2) && (this.map.details[xpos][ypos].getId() <= 4)) {
                    g.drawImage(this.rocks[(this.map.details[xpos][ypos].getId() - 1)], null, i * 32, j * 32);
                }
                if (this.map.details[xpos][ypos].getId() == 5) {
                    g.drawImage(this.trees[1], null, i * 32, j * 32);
                    g.drawImage(this.trees[0], null, (i - 1) * 32, j * 32);
                    g.drawImage(this.trees[2], null, (i - 1) * 32, (j - 1) * 32);
                    g.drawImage(this.trees[3], null, i * 32, (j - 1) * 32);
                    g.drawImage(this.trees[4], null, (i - 1) * 32, (j - 2) * 32);
                    g.drawImage(this.trees[5], null, i * 32, (j - 2) * 32);
                }*/
            }
        }
    }


    public void drawTiles(Graphics2D g, int x0, int y0, int x, int y){

        for(int k = 0; k < intro.map.length; k++) {
            int a = (int) Math.floor(new Maths().map(x0, 0, this.intro.width * 32, 0, this.intro.width));
            int b = (int) Math.floor(new Maths().map(x, 0, this.intro.width * 32, 0, this.intro.width));

            int c = (int) Math.floor(new Maths().map(y0,  0, this.intro.height * 32, 0, this.intro.height));
            int d = (int) Math.floor(new Maths().map(y,  0, this.intro.height * 32, 0, this.intro.height));
            if(a>0) a--;
            if(b<this.intro.width) b++;
            if(c>0) c--;
            if(d<this.intro.height) d++;
            Tiles[][] temp = intro.map[k];
            for (int i = a; i < b; i++) {
                for (int j = c; j < d; j++) {
                    if(temp[i][j].getId()>=0){
                        int xpos, ypos;
                        switch (temp[i][j].getTileset()){
                            case ("gameobjects"):
                                xpos = temp[i][j].getId() % (this.gameObjectTiles.getImg().getWidth() / 32);
                                ypos = (int) Math.floor(temp[i][j].getId() / (this.gameObjectTiles.getImg().getWidth() / 32));
                                if(ypos == 30){
                                    System.out.println("Stop");
                                }
                                g.drawImage(this.gameobjects[xpos][ypos], null, i * 32, j * 32);
                            break;
                            case ("basetiles"):
                                xpos = temp[i][j].getId();
                                g.drawImage(this.baseImages[xpos], null, i * 32, j * 32);
                                break;
                            case ("path"):
                                xpos = temp[i][j].getId();
                                g.drawImage(this.path[xpos], null, i * 32, j * 32);
                                break;
                            case ("house"):
                                xpos = temp[i][j].getId() % (this.houseTiles.getImg().getWidth() / 32);
                                ypos = temp[i][j].getId() / ((this.houseTiles.getImg().getWidth() / 32));
                                g.drawImage(this.house[xpos][ypos], null, i * 32, j * 32);
                                break;
                            case ("transitions"):
                                xpos = temp[i][j].getId() % (this.transitionTiles.getImg().getWidth() / 32);
                                ypos = temp[i][j].getId() / ((this.transitionTiles.getImg().getWidth() / 32));
                                g.drawImage(this.transitions[xpos][ypos], null, i * 32, j * 32);
                                break;
                        }
                    }
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

            tempObject.pos.x = (tempObject.abspos.x - this.cam.pos.x) + (float)(this.getWidth() - WIDTH)/2;
            tempObject.pos.y = (tempObject.abspos.y - this.cam.pos.y) + (float)(this.getHeight() - HEIGHT);
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

    public void createMiniMap(int width, int height, Tiles[][] map){
        BufferedImage img = new BufferedImage(width * 2, height * 2, 2);
        File f = null;
        for (int x = 0; x < width * 2; x += 2) {
            for (int y = 0; y < height * 2; y += 2) {
                int a = 255;
                int r = 0;
                int g = 0;
                int b = 0;
                switch (map[(x / 2)][(y / 2)].getId()) {
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
    }

    public void createGameObjects(){
        for (int i = 1; i < mapSize - 1; i++) {
            for (int j = 1; j < mapSize - 1; j++) {
                switch (this.map.details[i][j].getId()) {
                    case 2:
                        this.handler.addObject(new Stone2(i*32,j*32,ObjectId.Stone2,"src/resources/stone_1.png"));
                        break;
                    case 3:
                        this.handler.addObject(new Stone3(i*32,j*32,ObjectId.Stone3,"src/resources/stone_2.png"));
                        break;
                    case 4:
                        this.handler.addObject(new Stone4(i*32,j*32,ObjectId.Stone4,"src/resources/stone_3.png"));
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Window(800, 660, "Map generator", new Game());
    }
}
