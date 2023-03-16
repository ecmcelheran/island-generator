package map;

import java.util.List;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;


public class CircularMapBuilder implements MapBuilder{
    public CircularMapBuilder(){

    }

    @Override
    public Map build(Structs.Mesh aMesh, int outerR) {
        Map circularMap = new Map();
        circularMap.setStrictBounds(aMesh);
        circularMap.setRadius(outerR);
        //int outterR = 200;
        List<Structs.Vertex> verts = aMesh.getVerticesList();

        //for centered island
        double centerX = circularMap.max_x/2;
        circularMap.setCenterX(centerX);
        double centerY = circularMap.max_y/2;
        circularMap.setCenterY(centerY);


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
            if(Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2))<= outerR)//compare to radius
            {
                //System.out.println("found land!");
                circularMap.addTile(p);
            } 
        }
        return circularMap;
    }
    
}
