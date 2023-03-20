package map;

import java.util.List;
import java.util.ArrayList;
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
                circularMap.addLandTile(p);
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
        double x0 = max_x/2;
        double y0 = max_y/2;
        int numRemovals = rand.nextInt(1,2);
        double x1, y1, x2, y2;
        for(int i=0; i<numRemovals; i++){
            double startAngle = rand.nextDouble(0, Math.PI);
            double toAngle = rand.nextDouble(0, Math.PI);
            //double depth = rand.nextDouble(0, radius);
            x1 = x0 + radius*Math.cos(startAngle/2);
            y1 = y0 + radius*Math.sin(startAngle/2); 
            x2 = x0 + radius*Math.cos(toAngle/2);
            y2 = y0 + radius*Math.sin(toAngle/2); 
            ArrayList<Structs.Polygon> copy = new ArrayList<>();
            copy.addAll(circleMap.getLand());
            for(Structs.Polygon p : copy){
                double xP = aMesh.getVertices(p.getCentroidIdx()).getX();
                double yP = aMesh.getVertices(p.getCentroidIdx()).getY();
                double prod1 = (x1-x0)*(xP-x0)+(y1-y0)*(yP-y0);
                double prod2 = (x2-x0)*(xP-x0)+(y2-y0)*(yP-y0);
                if(prod1 > 0 && prod2 > 0){
                    circleMap.removeLandTile(p);
                    System.out.println("removed tiles!");
                }
            }
        }   
        return radialMap;
        
    }
}
