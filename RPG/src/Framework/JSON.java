package Framework;

import Maths.Maths;
import mapGenerator.Tiles;
import Object.Segment;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class JSON {
    JSONObject obj;
    public int width;
    public int height;
    public Tiles[][][] map;
    public HashMap<Integer, Tiles[][]> transArray;
    public ArrayList<Segment> seg;

    public JSON(String path){
        seg = new ArrayList<Segment>();
        try {
            String json = readFile(path);
            obj = new JSONObject(json);
            width = obj.getInt("width");
            height = obj.getInt("height");
            HashMap<String, Integer> ids = new HashMap<>();
            JSONArray tilesets = obj.getJSONArray("tilesets");
            for (int x = 0; x < tilesets.length(); x++) {
                JSONObject obj2 = tilesets.getJSONObject(x);
                switch (obj2.getString("name")) {
                    case ("gameobjects"):
                        ids.put("gameobjects", obj2.getInt("firstgid"));
                        break;
                    case ("basetiles"):
                        ids.put("basetiles", obj2.getInt("firstgid"));
                        break;
                    case ("path"):
                        ids.put("path", obj2.getInt("firstgid"));
                        break;
                    case ("house"):
                        ids.put("house", obj2.getInt("firstgid"));
                        break;
                    case ("transitions"):
                        ids.put("transitions", obj2.getInt("firstgid"));
                        break;
                }
            }


            JSONArray arr = obj.getJSONArray("layers");
            this.map = new Tiles[arr.length()-1][width][height];
            for (int i = 0; i < arr.length(); i++) {
                if (!arr.getJSONObject(i).getString("name").equals("_shadows")) {
                    JSONArray arr2 = arr.getJSONObject(i).getJSONArray("data");
                    this.map[i] = new Tiles[width][height];
                    int x = 0;
                    int y = 0;
                    for (int j = 0; j < arr2.length(); j++) {
                        if (j % (width) == 0 && j != 0) {
                            y++;
                            x = 0;
                        }
                        String name = arr.getJSONObject(i).getString("name");
                        this.map[i][x][y] = new Tiles();
                        this.map[i][x][y].setTileset(name.substring(name.indexOf("_") + 1));
                        this.map[i][x][y].setId((arr2.getInt(j) > 0) ? arr2.getInt(j) - ids.get(this.map[i][x][y].getTileset()) : -1);
                        this.map[i][x][y].setPos(x, y);

                        x++;
                    }
                }else{
                    JSONArray arr2 = arr.getJSONObject(i).getJSONArray("objects");
                    for(int j = 0; j< arr2.length(); j++){
                        int x0 = (int) arr2.getJSONObject(j).getDouble("x");
                        int y0 = (int) arr2.getJSONObject(j).getDouble("y");
                        JSONArray arr3 = arr2.getJSONObject(j).getJSONArray("polyline");
                        for(int k = 0; k < arr3.length(); k++){
                            int x1 = (int) arr3.getJSONObject(k).getDouble("x");
                            int y1 = (int) arr3.getJSONObject(k).getDouble("y");
                            int x2 = 0;
                            int y2 = 0;
                            if(k != arr3.length()-1){
                                x2 = (int) arr3.getJSONObject(k+1).getDouble("x");
                                y2 = (int) arr3.getJSONObject(k+1).getDouble("y");
                            }else{
                                x2 = (int) arr3.getJSONObject(0).getDouble("x");
                                y2 = (int) arr3.getJSONObject(0).getDouble("y");
                            }
                            seg.add(new Segment(x0, y0, x1, y1, x2, y2));
                        }
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try{
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();

        } finally {
            br.close();
        }
    }
}
