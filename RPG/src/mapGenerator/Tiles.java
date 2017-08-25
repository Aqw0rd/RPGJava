package mapGenerator;

public class Tiles {
    private int id = 0;
    private int type = 0;
    private int up = 0;
    private int down = 0;
    private int left = 0;
    private int right = 0;
    private boolean edge = false;
    private int binary = 0;

    public int getBinary() {
        return this.binary;
    }

    public void addBinary(int binary) {
        this.binary += binary;
    }

    private Tiles l = null;
    private Tiles r = null;
    private Tiles u = null;
    private Tiles d = null;
    private Tiles tl = null;
    private Tiles tr = null;
    private Tiles bl = null;
    private Tiles br = null;

    public Tiles getTl() {
        return this.tl;
    }

    public void setTl(Tiles tl) {
        this.tl = tl;
    }

    public Tiles getTr() {
        return this.tr;
    }

    public void setTr(Tiles tr) {
        this.tr = tr;
    }

    public Tiles getBl() {
        return this.bl;
    }

    public void setBl(Tiles bl) {
        this.bl = bl;
    }

    public Tiles getBr() {
        return this.br;
    }

    public void setBr(Tiles br) {
        this.br = br;
    }

    public Tiles getL() {
        return this.l;
    }

    public void setL(Tiles l) {
        this.l = l;
    }

    public Tiles getR() {
        return this.r;
    }

    public void setR(Tiles r) {
        this.r = r;
    }

    public Tiles getU() {
        return this.u;
    }

    public void setU(Tiles u) {
        this.u = u;
    }

    public Tiles getD() {
        return this.d;
    }

    public void setD(Tiles d) {
        this.d = d;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUp() {
        return this.up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return this.down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getLeft() {
        return this.left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return this.right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public boolean isEdge() {
        return this.edge;
    }

    public void setEdge(boolean edge) {
        this.edge = edge;
    }
}
