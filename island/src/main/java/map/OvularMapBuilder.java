package map;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import map.shape.MapBuilder;

public class OvularMapBuilder implements MapBuilder{

    @Override
    public Map build(Mesh aMesh, int R) {
        // int y = R;
        // int x = 2*y; 
        Map ovularMap = new Map();
        ovularMap.setStrictBounds(aMesh);
        ovularMap.setRadius(R);
        //int outterR = 200;
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        //for centered island
        double centerX = ovularMap.max_x/2;
        ovularMap.setCenterX(centerX);
        double centerY = ovularMap.max_y/2;
        ovularMap.setCenterY(centerY);
        double xDiff, yDiff;
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double margin = 2*Math.sqrt(((max_x*max_y)/aMesh.getPolygonsCount())/Math.PI);
        double increment = margin/6;
        for(double theta=0; theta<2*Math.PI; theta+=increment){
            //double circleAngle = theta*2;
            double x = R*Math.cos(theta);
            //double x = 2*xC;
            double y = (R/2)*Math.sin(theta);
            double ovalR = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
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
                if(Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2)) <= ovalR)//compare to radius
                {
                    //System.out.println("found land!");
                    ovularMap.addLandTile(p);
                 }//else{
                //     ovularMap.addOceanTile(p);
                // }
            }
        }
        ovularMap.findOcean(aMesh);
        ovularMap.findBorder(aMesh);
        return ovularMap;
    }
    
}
