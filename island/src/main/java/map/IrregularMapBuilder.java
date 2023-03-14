package map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.google.protobuf.Struct;

public class IrregularMapBuilder implements MapBuilder{

    @Override
    public Map build(Structs.Mesh aMesh) {
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        // List<Structs.Polygon> edges =  new ArrayList<>();
        // Structs.Polygon GX, LX, GY, LY;
        // for(Structs.Polygon p: aMesh.getPolygonsList()){
        //     for(Structs.Polygon p1: aMesh.getPolygonsList()){
        //         if(verts.get(p.getCentroidIdx()).getX()>verts.get(p1.getCentroidIdx()).getX()){
        //             GX = p;
        //         }
        //     }
            
        // }




        Map irregMap = new Map();
        ArrayList<Structs.Polygon> circle = new ArrayList<>();
        int outterR = 250; 
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double centerX = max_x/2;
        double centerY = max_y/2;
        double xDiff, yDiff;
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            Structs.Vertex centroid = verts.get(p.getCentroidIdx());
            if(centroid.getX() > centerX){
                xDiff = centroid.getX() - centerX;
            }else{
                xDiff = centerX - centroid.getX();
            }
            if(centroid.getY() > centerY){
                yDiff = centroid.getY() - centerY;
            }else{
                yDiff = centerY - centroid.getY();
            }
            if(Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2))<= outterR)//compare to radius
            {
                circle.add(p);
            } 
        }
        Random rand = new Random();
        //int j=0;
        for(int i=0; i<100; i++){
            Structs.Polygon targetPoly = circle.get(rand.nextInt(circle.size()));
            irregMap.addTile(targetPoly);
            List<Integer> neighbours = targetPoly.getNeighborIdxsList();
            for(int n: neighbours){
                irregMap.addTile(aMesh.getPolygons(n));
            }
            //j+=neighbours.size();
        }
        return irregMap;
    }

}