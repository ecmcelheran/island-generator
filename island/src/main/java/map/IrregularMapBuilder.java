package map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.google.protobuf.Struct;

public class IrregularMapBuilder implements MapBuilder{

    @Override
    public Map build(Structs.Mesh aMesh) {
        Map irregMap = new Map();
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        List<Structs.Polygon> edges =  new ArrayList<>();
        List<Structs.Polygon> border = new ArrayList<>();
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double margin = 2*Math.sqrt(((max_x*max_y)/aMesh.getPolygonsCount())/Math.PI); // avg diameter of a polygon 
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            Structs.Vertex centroid = verts.get(p.getCentroidIdx());
            if(centroid.getX() < margin || centroid.getX()>(max_x-margin)){
                edges.add(p);
            }else if(centroid.getY() < margin || centroid.getY()>(max_y-margin)){
                edges.add(p);
            }else{
                border.add(p);
            }
        }
        Random rand = new Random();
        for(int i=0; i<100; i++){
            Structs.Polygon targetPoly = border.get(rand.nextInt(border.size()));
            irregMap.addTile(targetPoly);
            List<Integer> neighbours = targetPoly.getNeighborIdxsList();
            for(int n: neighbours){
                if(!edges.contains(aMesh.getPolygons(n)))
                    irregMap.addTile(aMesh.getPolygons(n));
            }
        }
        return irregMap;
    }

}