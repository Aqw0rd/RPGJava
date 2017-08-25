package Framework;

import Maths.Maths;
import Maths.Vector2i;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import mapGenerator.Path;
import mapGenerator.Tiles;

public class Map {
    public Tiles[][] tile_ground;
    public Tiles[][] tile_elevation;
    public Tiles[][] details;
    public Tiles[][] corners;
    public Tiles[][] transition;
    public HashMap<Integer, Tiles[][]> transArray;
    public HashMap<Integer, Tiles[][]> cornerArray;
    public int dimension;
    public Path path;

    public Map(int mapSize, int[][] mapArray) {
        this.dimension = mapSize;
        this.tile_ground = new Tiles[mapSize][mapSize];
        this.tile_elevation = new Tiles[mapSize][mapSize];
        this.details = new Tiles[mapSize][mapSize];
        this.corners = new Tiles[mapSize][mapSize];
        this.transition = new Tiles[mapSize][mapSize];
        this.transArray = new HashMap();
        this.cornerArray = new HashMap();
        for (int i = 0; i < 10; i++) {
            this.transArray.put(Integer.valueOf(i), new Tiles[mapSize][mapSize]);
            this.cornerArray.put(Integer.valueOf(i), new Tiles[mapSize][mapSize]);
            for (int j = 0; j < ((Tiles[][]) this.transArray.get(Integer.valueOf(i))).length; j++) {
                for (int j2 = 0; j2 < ((Tiles[][]) this.transArray.get(Integer.valueOf(i))).length; j2++) {
                    ((Tiles[][]) this.transArray.get(Integer.valueOf(i)))[j][j2] = new Tiles();
                    ((Tiles[][]) this.transArray.get(Integer.valueOf(i)))[j][j2].setId(-1);
                    ((Tiles[][]) this.cornerArray.get(Integer.valueOf(i)))[j][j2] = new Tiles();
                    ((Tiles[][]) this.cornerArray.get(Integer.valueOf(i)))[j][j2].setId(-1);
                }
            }
        }
        addTiles(mapSize, mapArray);
        addDetails(mapSize);
        this.path = new Path(this);

        System.out.println("transArray: " + this.transArray.size());
        Iterator iterator = path.path.iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("unchecked")
            ArrayList<Vector2i> temp = (ArrayList<Vector2i>) iterator.next();
            for (int i = 0; i < temp.size(); i++) {
                int x = temp.get(i).x;
                int y = temp.get(i).y;
                if (tile_ground[x][y].getId() == 0) tile_ground[x][y].setId(5);
                else tile_ground[x][y].setId(2);
                if (this.details[x][y].getId() == 1) details[x][y].setId(0);

            }
        }
        properties(mapSize, false);
    }

    public void addTiles(int mapSize, int[][] mapArray) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                this.tile_ground[i][j] = new Tiles();
                this.tile_elevation[i][j] = new Tiles();
                this.details[i][j] = new Tiles();
                this.corners[i][j] = new Tiles();
                this.transition[i][j] = new Tiles();
                this.transition[i][j].setId(-1);
                this.corners[i][j].setId(-1);
                if (mapArray[i][j] < 2) {
                    this.tile_ground[i][j].setId(1);
                    this.tile_elevation[i][j].setId(0);
                } else {
                    this.tile_ground[i][j].setId(0);
                    this.tile_elevation[i][j].setId(0);
                }
            }
        }
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                this.tile_ground[i][j].setL(this.tile_ground[((i - 1) >= 0 ? (i - 1) : mapSize - 1)][j]);
                this.tile_ground[i][j].setR(this.tile_ground[((i + 1) < mapSize ? (i + 1) : 0)][j]);
                this.tile_ground[i][j].setU(this.tile_ground[i][((j - 1) >= 0 ? (j - 1) : mapSize - 1)]);
                this.tile_ground[i][j].setD(this.tile_ground[i][((j + 1) < mapSize ? (j + 1) : 0)]);

                this.tile_ground[i][j].setTl(this.tile_ground[((i - 1) >= 0 ? (i - 1) : mapSize - 1)][((j - 1) >= 0 ? (j - 1) : mapSize - 1)]);
                this.tile_ground[i][j].setTr(this.tile_ground[((i + 1) < mapSize ? (i + 1) : 0)][((j - 1) >= 0 ? (j - 1) : mapSize - 1)]);
                this.tile_ground[i][j].setBl(this.tile_ground[((i - 1) >= 0 ? (i - 1) : mapSize - 1)][((j + 1) < mapSize ? (j + 1) : 0)]);
                this.tile_ground[i][j].setBr(this.tile_ground[((i + 1) < mapSize ? (i + 1) : 0)][((j + 1) < mapSize ? (j + 1) : 0)]);
            }
        }
    }

    public void checkSelf(int mapSize) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                // Checking if tiles next to itself are equal to itself
                try {
                    if (tile_ground[i][j].getId() < tile_ground[i][j].getL().getId()) {
                        int id = (int) new Maths().map(tile_ground[i][j].getL().getId(), 1, 6, 0, 10);
                        transArray.get(id)[i][j].addBinary(0b1);
                        tile_ground[i][j].addBinary(0b1);
                    }
                } catch (NullPointerException e) {
                    //System.out.println(e);
                }
                try {
                    if (tile_ground[i][j].getId() < tile_ground[i][j].getU().getId()) {
                        int id = (int) new Maths().map(tile_ground[i][j].getU().getId(), 1, 6, 0, 10);
                        transArray.get(id)[i][j].addBinary(0b10);
                        tile_ground[i][j].addBinary(0b10);
                    }
                } catch (NullPointerException e) {
                    //System.out.println(e);
                }
                try {
                    if (tile_ground[i][j].getId() < tile_ground[i][j].getR().getId()) {
                        int id = (int) new Maths().map(tile_ground[i][j].getR().getId(), 1, 6, 0, 10);
                        transArray.get(id)[i][j].addBinary(0b100);
                        tile_ground[i][j].addBinary(0b100);
                    }
                } catch (NullPointerException e) {
                    //System.out.println(e);
                }
                try {
                    if (tile_ground[i][j].getId() < tile_ground[i][j].getD().getId()) {
                        int id = (int) new Maths().map(tile_ground[i][j].getD().getId(), 1, 6, 0, 10);
                        transArray.get(id)[i][j].addBinary(0b1000);
                        tile_ground[i][j].addBinary(0b1000);
                    }
                } catch (NullPointerException e) {
                    //System.out.println(e);
                }
            }
        }
    }

    public void checkCorners(int mapSize) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                int id = tile_ground[i][j].getId();

                Integer u = tile_ground[i][j].getU().getId();//(tile_ground[i][j].getU() != null ? tile_ground[i][j].getU().getId() : null);
                Integer d = tile_ground[i][j].getD().getId(); //(tile_ground[i][j].getD() != null ? tile_ground[i][j].getD().getId() : null);
                Integer l = tile_ground[i][j].getL().getId(); //(tile_ground[i][j].getL() != null ? tile_ground[i][j].getL().getId() : null);
                Integer r = tile_ground[i][j].getR().getId(); //(tile_ground[i][j].getR() != null ? tile_ground[i][j].getR().getId() : null);

                Integer tl = tile_ground[i][j].getTl().getId(); //(tile_ground[i][j].getTl() != null ? tile_ground[i][j].getTl().getId() : null);
                Integer tr = tile_ground[i][j].getTr().getId(); //(tile_ground[i][j].getTr() != null ? tile_ground[i][j].getTr().getId() : null);
                Integer bl = tile_ground[i][j].getBl().getId(); //(tile_ground[i][j].getBl() != null ? tile_ground[i][j].getBl().getId() : null);
                Integer br = tile_ground[i][j].getBr().getId(); //(tile_ground[i][j].getBr() != null ? tile_ground[i][j].getBr().getId() : null);
                int xend = i;
                int yend = j;
                int xstart = i;
                int ystart = j;
                if (i == 0) xend = mapSize;
                if (j == 0) yend = mapSize;
                if (i == mapSize - 1) xstart = -1;
                if (j == mapSize - 1) ystart = -1;
                if (id > tl && tl == u && tl == l) {
                    id = (int) new Maths().map(id, 1, 6, 0, 10);
                    cornerArray.get(id + 1)[xend - 1][yend - 1].addBinary(0b100);
                    tile_ground[i][j].addBinary(0b100);
                    id = tile_ground[i][j].getId();
                }
                if (id > tr && tr == u && tr == r) {
                    id = (int) new Maths().map(id, 1, 6, 0, 10);
                    cornerArray.get(id + 1)[xstart + 1][yend - 1].addBinary(0b1000);
                    tile_ground[i][j].addBinary(0b10);
                    id = tile_ground[i][j].getId();
                }

                if (id > bl && bl == d && bl == l) {
                    id = (int) new Maths().map(id, 1, 6, 0, 10);
                    cornerArray.get(id + 1)[xend - 1][ystart + 1].addBinary(0b10);
                    tile_ground[i][j].addBinary(0b1000);
                    id = tile_ground[i][j].getId();
                }
                if (id > br && br == d && br == r) {
                    id = (int) new Maths().map(id, 1, 6, 0, 10);
                    cornerArray.get(id + 1)[xstart + 1][ystart + 1].addBinary(0b1);
                    tile_ground[i][j].addBinary(0b100);
                    id = tile_ground[i][j].getId();
                }
            }
        }
    }

    public void properties(int mapSize, boolean elevation) {
        checkSelf(mapSize);
        checkCorners(mapSize);
    }

    public void addDetails(int mapSize) {
        Random rg = new Random();
        int r = 0;
        for (int i = 1; i < mapSize - 1; i++) {
            for (int j = 1; j < mapSize - 1; j++) {
                if (this.tile_ground[i][j].getId() == 1) {
                    r = rg.nextInt(100) + 1;
                    if ((r > 99) && (!this.tile_ground[i][j].isEdge())) {
                        this.details[i][j].setId(5);
                    } else {
                        r = rg.nextInt(6) + 1;
                        int g1 = this.details[(i - 1)][j].getId() == 1 ? 1 : 0;
                        int g2 = this.details[i][(j - 1)].getId() == 1 ? 1 : 0;
                        r += g2 + g1;
                        if (r > 5) {
                            this.details[i][j].setId(1);
                        } else {
                            this.details[i][j].setId(0);
                            r = rg.nextInt(100) + 1;
                            if (r > 99) {
                                this.details[i][j].setId(rg.nextInt(3) + 2);
                            }
                        }
                    }
                }
            }
        }
    }
}
