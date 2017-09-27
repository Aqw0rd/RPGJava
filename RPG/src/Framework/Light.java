package Framework;

import Maths.Vector2f;
import Maths.Vector3f;
import mapGenerator.ObjectId;
import Object.Segment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;


public class Light {
    ArrayList<LightRay> rays;
    int length = 100;
    ArrayList<Polygon> shadow;
    float x;
    float y;
    int num;

    public Light(int num, GameObject player, int width, int height, ArrayList<Segment> seg){
        x = player.getPos().x + player.getBounds().width/2;
        y = player.getPos().y + player.getBounds().height/2;
        shadow = new ArrayList<>();
        rays = new ArrayList<>();
        this.num = num;
        createRays(seg);

    }

    public void createRays(ArrayList<Segment> seg){
        rays.clear();
        for(float angle = 0; angle < Math.PI*2; angle+= (Math.PI*2)/num){
            float dx = (float) Math.cos(angle) + x;
            float dy = (float) Math.sin(angle) + y;
            Vector3f closestIntersection = null;
            for(Segment segment: seg){
                Vector3f intersection = getIntersection(x,y,dx,dy,segment);
                if(intersection == null) continue;
                if(closestIntersection == null || intersection.z < closestIntersection.z) closestIntersection = intersection;
            }
            if(closestIntersection != null) rays.add(new LightRay(x, y,closestIntersection.x , closestIntersection.y));
        }
        updateShadow();
    }

    public void tick(LinkedList<GameObject> objects, ArrayList<Segment> seg){
        for(GameObject g:objects){
            if(g.getId() == ObjectId.Player){
                x = g.getPos().x + g.getBounds().width/2;
                y = g.getPos().y + g.getBounds().height/2;
                createRays(seg);
            }
        }

    }

    void updateShadow(){
        shadow.clear();
        Polygon p;
        for(int i = 0; i<rays.size(); i++){
            p = new Polygon();

            p.addPoint((int)x,(int)y);
            p.addPoint((int)rays.get(i).xDest,(int)rays.get(i).yDest);
            if(i<rays.size()-1) p.addPoint((int)rays.get(i+1).xDest,(int)rays.get(i+1).yDest);
            else p.addPoint((int)rays.get(0).xDest,(int)rays.get(0).yDest);
            shadow.add(p);


        }
    }


    Vector3f getIntersection(float x, float y, float bx, float by, Segment segment){
        float rx = x;
        float ry = y;
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
        float T2 = (rdx*(sy-ry) + rdy*(rx-sx))/(sdx*rdy - sdy*rdx);
        float T1 = (sx+sdx*T2-rx)/rdx;
        if(T1<0)return null;
        if(T2<0 || T2>1) return null;
        return new Vector3f(rx+rdx*T1, ry+rdy*T1, T1);

                //param: T1

    }


}
