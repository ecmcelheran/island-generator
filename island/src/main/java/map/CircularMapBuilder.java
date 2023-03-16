package map;

import java.util.List;
import java.util.Random;
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
     
    public Map radial(Structs.Mesh aMesh, Map circleMap){
        Map radialMap = new Map();
        Random rand = new Random();
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Polygon p: circleMap.getLand()) {
            max_x = (Double.compare(max_x, aMesh.getVertices(p.getCentroidIdx()).getX()) < 0? aMesh.getVertices(p.getCentroidIdx()).getX(): max_x);
            max_y = (Double.compare(max_y, aMesh.getVertices(p.getCentroidIdx()).getY()) < 0? aMesh.getVertices(p.getCentroidIdx()).getY(): max_y);

        }
        //double radius = max_x/2;
        double radius = 200;
        double centerX = max_x/2;
        double centerY = max_y/2;
        int numRemovals = rand.nextInt(1,7);
        for(int i=0; i<numRemovals; i++){
             
        }
        double startAngle = rand.nextDouble(0, 2*Math.PI);
        double toAngle = rand.nextDouble(0, 2*Math.PI);
        double depth = rand.nextDouble(0, radius);
         
        return radialMap;
        
    }
}
