package Framework;

import Maths.Vector2f;
import Maths.Vector2i;
import Maths.Vector3f;
import mapGenerator.ObjectId;
import Object.Segment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.*;

/**
 * Followed tutorial on
 * http://ncase.me/sight-and-light/
 */


public class Light {
    ArrayList<LightRay> rays;
    int length = 100;
    Polygon light;
    ArrayList<Vector2i> points;
    ArrayList<Float> angles;
    BufferedImage shadow;
    Raster reset;
    Graphics2D g;
    int[] rgb;
    float x;
    float y;
    int num;

    public Light(int num, GameObject player, int width, int height, ArrayList<Segment> seg){
        points = getUnique(seg);
        angles = getAngles(points);
        shadow = new BufferedImage(width*32, height*32, 2);
        g = shadow.createGraphics();
        g.setPaint(Color.BLACK);
        g.fillRect(0,0,shadow.getWidth(),shadow.getHeight());
        /*rgb = new int[shadow.getWidth()*height*32*32];
        for(int i = 0; i < shadow.getWidth(); i++){
            for(int j = 0; j < shadow.getWidth(); j++){
                int p = 255 << 24;
                shadow.setRGB(i,j,p);
                rgb[i*shadow.getWidth() + j] = p;
            }
        }*/
        reset = shadow.copyData(null);
        x = player.getPos().x + player.getBounds().width/2;
        y = player.getPos().y + player.getBounds().height/2;
        light = new Polygon();
        rays = new ArrayList<>();
        this.num = num;
        createRays(seg);

    }

    public void createRays(ArrayList<Segment> seg){
        rays.clear();
        //points.clear();
        angles.clear();
        //points = getUnique(seg);
        angles = getAngles(points);
        for(int i = 0; i < this.angles.size(); i++){
            float angle = angles.get(i);
            float dx = (float) Math.cos(angle) + x;
            float dy = (float) Math.sin(angle) + y;
            Vector3f closestIntersection = null;
            for(Segment segment: seg){
                Vector3f intersection = getIntersection(x,y,dx,dy,segment);
                if(intersection == null) continue;
                if(closestIntersection == null || intersection.z < closestIntersection.z) closestIntersection = intersection;
            }
            if(closestIntersection != null){
                rays.add(new LightRay(x, y,closestIntersection.x , closestIntersection.y, angle));
                //addRays(closestIntersection.x, closestIntersection.y, angles.get(i+1), seg);
                //addRays(closestIntersection.x, closestIntersection.y, angles.get(i+2), seg);
            }


        }

        Collections.sort(rays, new Comparator<LightRay>() {
            @Override
            public int compare(LightRay o1, LightRay o2) {
                if(o1.angle > o2.angle) return 1;
                else if(o1.angle < o2.angle) return -1;
                else return 0;
            }
        });


    }

    public void tick(LinkedList<GameObject> objects, ArrayList<Segment> seg){
        for(GameObject g:objects){
            if(g.getId() == ObjectId.Player){
                x = g.getPos().x + g.getBounds().width/2;
                y = g.getPos().y + g.getBounds().height/2;

            }
        }
        createRays(seg);
        updateLight();
        updateShadows();

    }

    public void addRays(float x, float y, float angle, ArrayList<Segment> seg){
        float dx = (float) Math.cos(angle) + x;
        float dy = (float) Math.sin(angle) + y;
        Vector3f closestIntersection = null;
        for(Segment segment: seg){
            Vector3f intersection = getIntersection(x,y,dx,dy,segment);
            if(intersection == null) continue;
            if(closestIntersection == null || intersection.z < closestIntersection.z) closestIntersection = intersection;
        }
        if(closestIntersection != null){
            rays.add(new LightRay(x, y,closestIntersection.x , closestIntersection.y, angle));
        }
    }

    void updateLight(){
        Polygon p = new Polygon();
        /*float d = (float) Math.sqrt( Math.pow(rays.get(0).xDest - this.x,2) +
                                     Math.pow(rays.get(0).yDest - this.y,2));
        float adjustedX = 0;
        float t = 0;
        float adjustedY = 0;
        if(d>length) {
            t = this.length / d;
            adjustedX = (1 - t) * this.x + t * rays.get(0).xDest;
            adjustedY = (1 - t) * this.y + t * rays.get(0).yDest;
            p.addPoint((int) adjustedX, (int) adjustedY);
        }
        else*/ p.addPoint((int) rays.get(0).xDest, (int)  rays.get(0).yDest);

        for(int i = 1; i<rays.size(); i++){

           /* d = (float) Math.sqrt( Math.pow(rays.get(i).xDest - this.x,2) +
                    Math.pow(rays.get(i).yDest - this.y,2));
            if(d>length) {
                t = this.length / d;
                adjustedY = (1 - t) * this.y + t * rays.get(i).yDest;
                adjustedX = (1 - t) * this.x + t * rays.get(i).xDest;

                p.addPoint((int) adjustedX, (int) adjustedY);
            }
            else*/ p.addPoint((int) rays.get(i).xDest, (int)  rays.get(i).yDest);
            //if(i<rays.size()-1) p.addPoint((int)rays.get(i+1).xDest,(int)rays.get(i+1).yDest);
            //else p.addPoint((int)rays.get(0).xDest,(int)rays.get(0).yDest);
        }
        light = p;
    }

    ArrayList<Vector2i> getUnique(ArrayList<Segment> seg){
        ArrayList<Vector2i> points = new ArrayList<>();
        for(Segment segment:seg) {
            points.add(segment.a);
            //points.add(segment.b);
        }
        for(int i = 0; i < points.size(); i++){
            Vector2i p = points.get(i);
            for(int j = 0; j < points.size(); j++){
                if(j != i && p.x == points.get(j).x && p.y == points.get(j).y){
                    points.remove(points.get(j));
                    j = 0;
                }
            }
        }

        return points;
    }

    ArrayList<Float> getAngles(ArrayList<Vector2i> points){
        ArrayList<Float> tempAngles = new ArrayList<>();

        for(Vector2i p: points){
            float angle = (float) Math.atan2(p.y-this.y,p.x-this.x);
            tempAngles.add(angle);
            tempAngles.add(angle-0.0001f);
            tempAngles.add(angle+0.0001f);
        }


        return tempAngles;
    }


    Vector3f getIntersection(float x, float y, float bx, float by, Segment segment){
        float rdx = bx-x;
        float rdy = by-y;

        float sx = segment.a.x;
        float sy = segment.a.y;
        float sdx = segment.b.x-segment.a.x;
        float sdy = segment.b.y-segment.a.y;

        float r_mag = (float)Math.sqrt(rdx*rdx+rdy*rdy);
        float s_mag =(float) Math.sqrt(sdx*sdx+sdy*sdy);
        if(rdx/r_mag==sdx/s_mag && rdy/r_mag==sdy/s_mag) {
            return null;
        }
        float T2 = (rdx*(sy-y) + rdy*(x-sx))/(sdx*rdy - sdy*rdx);
        float T1 = (sx+sdx*T2-x)/rdx;
        if(T1<0)return null;
        if(T2<0 || T2>1) return null;
        return new Vector3f(x+rdx*T1, y+rdy*T1, T1);

                //param: T1

    }

    void updateShadows(){
        //shadow.setData(reset);

        g.setPaint(Color.BLACK);
        g.fillRect(0,0,shadow.getWidth(),shadow.getHeight());
        int xMin = (int)this.x-length;
        int yMin = (int)this.y-length;
        int xMax = (int)this.x+length;
        int yMax = (int)this.y+length;
        for(int i = xMin; i< xMax;i++){
            for(int j = yMin; j< yMax;j++){
                float d = (float) Math.sqrt(Math.pow(i - this.x, 2) +
                        Math.pow(j - this.y, 2));
                if(light.contains(i,j) && d <= length) {
                    int a = (int) new Maths.Maths().map(d, 0, length, 0, 255);
                    int p = a << 24;
                    shadow.setRGB(i, j, p);
                }
            }
        }

    }


}
