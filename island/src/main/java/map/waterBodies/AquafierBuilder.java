package map.waterBodies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import map.Map;

public class AquafierBuilder implements WaterBuilder{

    @Override
    public Map build(Mesh aMesh, Map map, int numUnits, long seed) {
        ArrayList<Structs.Polygon> border = map.getBorder();
        ArrayList<Structs.Polygon> innerLand =  map.getInnerLand();
        HashMap<Integer, Double> elevations = map.getElevation();
        
        Random rand = new Random();
        rand.setSeed(seed);

        for(int n=0; n<numUnits; n++){
            double minElevation = Double.MAX_VALUE;
            //Structs.Polygon targetPoly = innerLand.get(rand.nextInt(innerLand.size()));
            Structs.Polygon targetPoly = null;
            for (Structs.Polygon polygon : innerLand) {
                double elevation = elevations.get(aMesh.getPolygonsList().indexOf(polygon));
                if (elevation < minElevation && !map.getAquaf().contains(polygon) ) {
                    minElevation = elevation;
                    targetPoly = polygon;
                }
            }
            if (targetPoly != null) {
                map.addAquafTile(targetPoly);
                map.removeLandTile(targetPoly);
                List<Integer> neighbours = targetPoly.getNeighborIdxsList();
                for(int i: neighbours){
                    Structs.Polygon neighbourPoly = aMesh.getPolygons(i);
                    if(!border.contains(neighbourPoly) && !map.getAquaf().contains(neighbourPoly)) {
                        double neighbourElevation = elevations.get(i);
                        if (neighbourElevation <= minElevation) {
                      
                          map.addAquafTile(aMesh.getPolygons(i));
                         }
                  }
              }
           }
        }

       return map;
    }
    
    
}
