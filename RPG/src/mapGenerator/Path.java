package mapGenerator;

import Maths.Maths;
import Maths.Vector2i;
import Framework.Map;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Path {
    public HashSet<ArrayList<Vector2i>> path;
    HashSet<PathTile> openList;
    HashSet<PathTile> notCheckedList;
    HashSet<Vector2i> closedList;

    public Path(Map map) {
        this.path = new HashSet();
        int[][] region = createPlace(map.dimension);
        int[][] crossroads = createCrossroads(region);
        int[][] obstacles = obstacleMap(map);
        this.notCheckedList = new HashSet();
        this.openList = new HashSet();
        this.closedList = new HashSet();
        ArrayList<PathTile> points = createPoints(region, obstacles);
        ArrayList<PathTile> crossroadPoints = createCrossroadsPoints(crossroads, obstacles, region);
        for (int i = 0; i < points.size() - 1; i++) {
            this.path.add(astar((PathTile) points.get(i), (PathTile) points.get(i + 1), map.dimension, obstacles));
        }
    }

    public ArrayList<PathTile> createPoints(int[][] region, int[][] obstacles) {
        ArrayList<PathTile> points = new ArrayList();
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region.length; j++) {
                if (region[i][j] == 1) {
                    int x = new Maths().randomInt(j * 20, (j + 1) * 20 - 1);
                    int y = new Maths().randomInt(i * 20, (i + 1) * 20 - 1);
                    while (obstacles[x][y] == 1) {
                        x = new Maths().randomInt(j * 20, (j + 1) * 20 - 1);
                        y = new Maths().randomInt(i * 20, (i + 1) * 20 - 1);
                    }
                    PathTile point = new PathTile();
                    point.setPos(new Vector2i(x, y));
                    points.add(point);
                }
            }
        }
        return points;
    }

    public ArrayList<PathTile> createCrossroadsPoints(int[][] crossroads, int[][] obstacles, int[][] region) {
        ArrayList<PathTile> points = new ArrayList();
        for (int i = 0; i < crossroads.length; i++) {
            String reg = "";
            for (int j = 0; j < crossroads.length; j++) {
                reg = reg + crossroads[i][j];
                if (crossroads[i][j] == 1) {
                    int x1 = (int) new Maths().map(i, 0.0F, crossroads.length, 0.0F, region.length);
                    int y1 = (int) new Maths().map(j, 0.0F, crossroads.length, 0.0F, region.length);
                    int x = new Maths().randomInt(y1 * 20, (y1 + 1) * 20 - 1);
                    int y = new Maths().randomInt(x1 * 20, (x1 + 1) * 20 - 1);
                    while (obstacles[x][y] == 1) {
                        x = new Maths().randomInt(y1 * 20, (y1 + 1) * 20 - 1);
                        y = new Maths().randomInt(x1 * 20, (x1 + 1) * 20 - 1);
                    }
                    PathTile point = new PathTile();
                    point.setPos(new Vector2i(x, y));
                    points.add(point);
                }
            }
            System.out.println(reg);
        }
        return points;
    }

    public int[][] createCrossroads(int[][] region) {
        int[][] crossroads = new int[region.length / 5][region.length / 5];
        for (int i = 0; i < crossroads.length; i++) {
            for (int j = 0; j < crossroads.length; j++) {
                crossroads[i][j] = 0;
            }
        }
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region.length; j++) {
                if (region[i][j] == 1) {
                    int x = (int) new Maths().map(i, 0.0F, region.length, 0.0F, crossroads.length);
                    int y = (int) new Maths().map(j, 0.0F, region.length, 0.0F, crossroads.length);
                    crossroads[x][y] = 1;
                }
            }
        }
        return crossroads;
    }

    public int[][] createPlace(int mapSize) {
        int[][] region = new int[mapSize / 20][mapSize / 20];
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region.length; j++) {
                region[i][j] = (new Random().nextInt(100) > 98 ? 1 : 0);
            }
        }
        return region;
    }

    public int[][] obstacleMap(Map map) {
        int[][] obstacles = new int[map.tile_ground.length][map.tile_ground.length];
        for (int i = 0; i < obstacles.length; i++) {
            for (int j = 0; j < obstacles.length; j++) {
                obstacles[i][j] = 0;
            }
        }
        return obstacles;
    }

    public ArrayList<Vector2i> astar(PathTile a, PathTile b, int mapSize, int[][] obstacles) {
        ArrayList<PathTile> used = new ArrayList();
        boolean finished = false;
        this.notCheckedList.clear();
        this.openList.clear();
        this.closedList.clear();
        this.notCheckedList.add(a);
        this.openList.add(a);
        used.add(a);
        used.add(b);
        PathTile current = a;
        Iterator<PathTile> iterator;
        while (!current.equals(b)) {
            int tempF = 0;
            boolean first = true;
            iterator = this.openList.iterator();
            if (this.notCheckedList.size() > 0) {
                iterator = this.notCheckedList.iterator();
            }
            ArrayList<PathTile> deleteList = new ArrayList();
            while (iterator.hasNext()) {
                PathTile temp = (PathTile) iterator.next();
                if (this.notCheckedList.size() > 0) {
                    deleteList.add(temp);
                }
                if ((first) || (temp.getF() < tempF)) {
                    first = false;
                    tempF = temp.getF();
                    current = temp;
                }
            }
            for (PathTile p : deleteList) {
                this.notCheckedList.remove(p);
            }
            int x = current.getPos().x;
            int y = current.getPos().y;
            if ((x != 0) && (x != mapSize - 1) && (y != 0) && (y != mapSize - 1)) {
                if ((obstacles[(x + 1)][y] == 0) && (!this.closedList.contains(new Vector2i(x + 1, y)))) {
                    PathTile adjacent = new PathTile();
                    adjacent.setPos(new Vector2i(x + 1, y));
                    adj(adjacent, 10, current, b, used);
                }
                if ((obstacles[(x - 1)][y] == 0) && (!this.closedList.contains(new Vector2i(x - 1, y)))) {
                    PathTile adjacent = new PathTile();
                    adjacent.setPos(new Vector2i(x - 1, y));
                    adj(adjacent, 10, current, b, used);
                }
                if ((obstacles[x][(y + 1)] == 0) && (!this.closedList.contains(new Vector2i(x, y + 1)))) {
                    PathTile adjacent = new PathTile();
                    adjacent.setPos(new Vector2i(x, y + 1));
                    adj(adjacent, 10, current, b, used);
                }
                if ((obstacles[x][(y - 1)] == 0) && (!this.closedList.contains(new Vector2i(x, y - 1)))) {
                    PathTile adjacent = new PathTile();
                    adjacent.setPos(new Vector2i(x, y - 1));
                    adj(adjacent, 10, current, b, used);
                }
            }
            this.closedList.add(current.getPos());
            iterator = this.openList.iterator();
            while (iterator.hasNext()) {
                PathTile temp = (PathTile) iterator.next();
                if ((temp.getH() == 0) && (!current.equals(a))) {
                    finished = true;
                    b.setParent(temp);
                    break;
                }
            }
            this.openList.remove(current);
            if (finished) {
                break;
            }
        }
        ArrayList<Vector2i> temppath = new ArrayList();
        while (!current.getParent().equals(a)) {
            temppath.add(current.getParent().getPos());
            current = current.getParent();
        }
        for (PathTile s : used) {
            s.setParent(null);
        }
        return temppath;
    }

    public void adj(PathTile a, int g, PathTile current, PathTile b, ArrayList<PathTile> used) {
        if (!this.openList.contains(a)) {
            a.setParent(current);
            a.setG(g + a.getParent().getG());
            int H = Math.abs(a.getPos().x - b.getPos().x) + Math.abs(a.getPos().y - b.getPos().y);
            H *= 10;
            a.setH(H);
            a.setF(a.getG() + a.getH());
            this.openList.add(a);
            this.notCheckedList.add(a);
            used.add(a);
        } else if (a.getG() > current.getG() + g) {
            a.setParent(current);
            a.setG(current.getG() + g);
            a.setF(a.getG() + a.getH());
        }
    }
}
