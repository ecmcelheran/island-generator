package elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import java.util.*;
import map.Map;


public class MountainBuilder implements Elevation{
    public HashMap<Integer,Double> assignElevations(Map island, Structs.Mesh aMesh){
        HashMap<Integer,Double> elevations = new HashMap<>();
        //for each polygon find distance to edge
        double temp_dist;
        double dist = 1000000000.0;
        Structs.Vertex v1,v2;
        for(Structs.Polygon p: island.getLand()){
            int c = p.getCentroidIdx();
            v1 = aMesh.getVertices(c);
            for (Structs.Polygon edges : island.getBorder()) {
                int e = edges.getCentroidIdx();
                v2 = aMesh.getVertices(e);
                temp_dist = distance(v1, v2);
                if (temp_dist < dist) {
                    dist = temp_dist;
                }
            }
            elevations.put(aMesh.getPolygonsList().indexOf(p),dist);
            dist = 100000000.0;
        }

        return elevations;
    }


    public double distance(Structs.Vertex v1, Structs.Vertex v2){
        double y_dist, x_dist;
        if(v1.getY()>v2.getY()){
            y_dist =v1.getY()-v2.getY();
        }
        else{
            y_dist =v2.getY()-v1.getY();
        }
        if(v1.getX()>v2.getX()){
            x_dist =v1.getX()-v2.getX();
        }
        else{
            x_dist =v2.getX()-v1.getX();
        }
        return Math.sqrt(Math.pow(x_dist,2) + Math.pow(y_dist,2));
    }

}
