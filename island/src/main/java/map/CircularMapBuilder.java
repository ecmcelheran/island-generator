package map;

import java.util.List;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;


public class CircularMapBuilder implements MapBuilder{
    public CircularMapBuilder(){

    }

    @Override
    public Map build(Structs.Mesh aMesh) {
        Map circularMap = new Map();
        int outterR = 200; 
        List<Structs.Vertex> verts = aMesh.getVerticesList();
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
                //System.out.println("found land!");
                circularMap.addTile(p);
            } 
        }
        return circularMap;
    }
    
}
