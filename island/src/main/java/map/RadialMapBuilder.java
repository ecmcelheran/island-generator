package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import map.shape.CircularMapBuilder;
import map.shape.MapBuilder;

public class RadialMapBuilder implements MapBuilder{

    @Override
    public Map build(Mesh aMesh, int R) {
        CircularMapBuilder circlebuild = new CircularMapBuilder();
        Map radialMap = circlebuild.build(aMesh, R);
        Random rand = new Random();
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        //int branches = rand.nextInt(3,4);
        int branches = 4;
        double centerX = radialMap.max_x/2;
        double centerY = radialMap.max_y/2;
        double xDiff, yDiff;
        double randStartAngle; double randEndAngle;
        for(int i=0; i<branches; i++){
            double randR = rand.nextDouble(R/4, R);
            if(branches%4==0){
                randStartAngle = rand.nextDouble(0, 45);
                randEndAngle = rand.nextDouble(randStartAngle, 90);
            }else if(branches%4 ==1){
                randStartAngle = rand.nextDouble(90, 135);
                randEndAngle = rand.nextDouble(randStartAngle, 180);
            }else if(branches%4 ==2){
                randStartAngle = rand.nextDouble(180, 225);
                randEndAngle = rand.nextDouble(randStartAngle, 270);
            }else{
                randStartAngle = rand.nextDouble(270, 315);
                randEndAngle = rand.nextDouble(randStartAngle, 360);
            }
            // if(!(branches%4 == 0)){
            //     randStartAngle = (branches%4)*randStartAngle;
            //     randEndAngle = (branches%4)*randEndAngle;
            // }
            for(Structs.Polygon p : aMesh.getPolygonsList()){
                Structs.Vertex centroid = verts.get(p.getCentroidIdx());
                double x = centroid.getX();
                double y = centroid.getY();
                double pointTheta = 0;
                if(x> centerX){
                    xDiff = x - centerX;
                    if(y > centerY){
                        yDiff = y - centerY;
                        pointTheta =  Math.atan(yDiff/xDiff);
                    }else{
                        yDiff = centerY - y; 
                        pointTheta =  Math.atan(xDiff/yDiff)+270;
                    }
                }else{
                    xDiff = centerX - x;
                    if(y > centerY){
                        yDiff = y - centerY;
                        pointTheta =  Math.atan(xDiff/yDiff)+90;
                    }else{
                        yDiff = centerY - y;
                        pointTheta =  Math.atan(yDiff/xDiff)+180;
                    }
                }
                if(Math.sqrt(Math.pow(xDiff,2)+Math.pow(yDiff,2)) >= randR && (randStartAngle<=pointTheta && pointTheta<= randEndAngle))//compare to radius
                {
                    //System.out.println("found land!");
                    radialMap.removeLandTile(p);
                }//else{
                    //     radialMap.addOceanTile(p);
                    // }
                
            }

        }
        return radialMap;
    }
    
    
}
