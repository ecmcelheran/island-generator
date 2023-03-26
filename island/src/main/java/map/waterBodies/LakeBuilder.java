package map.waterBodies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

public class LakeBuilder implements WaterBuilder {

    @Override
    public Map build(Structs.Mesh aMesh, Map map, int numUnits, long seed) {
        //ArrayList<Structs.Polygon> land = map.getLand();
        
        ArrayList<Structs.Polygon> border = map.getBorder();
        ArrayList<Structs.Polygon> innerLand =  map.getInnerLand();
        HashMap<Integer, Double> elevations = map.getElevation();
        
        Random rand = new Random();
        rand.setSeed(seed);


        int atLeast = numUnits/2;
        int numLakes = rand.nextInt(atLeast, numUnits);
        for(int n=0; n<numLakes; n++){
            double minElevation = Double.MAX_VALUE;
            Structs.Polygon targetPoly = null;// innerLand.get(rand.nextInt(innerLand.size()));
            for (Structs.Polygon polygon : innerLand) {
                double elevation = elevations.get(aMesh.getPolygonsList().indexOf(polygon));
                if (elevation < minElevation && !map.getLakes().contains(polygon)) {
                    minElevation = elevation;
                    targetPoly = polygon;
                }
            }
            if (targetPoly != null) {
            //Structs.Polygon targetPoly = innerLand.get(rand.nextInt(innerLand.size()));
                map.addLakeTile(targetPoly);
                map.removeLandTile(targetPoly);
                List<Integer> neighbours = targetPoly.getNeighborIdxsList();
                for(int i: neighbours){
                    Structs.Polygon neighbourPoly = aMesh.getPolygons(i);
                    if(!border.contains(neighbourPoly) && !map.getLakes().contains(neighbourPoly)){
                       double neighbourElevation = elevations.get(i);
                       if (neighbourElevation <= minElevation) {
                        map.removeLandTile(neighbourPoly);
                        map.addLakeTile(neighbourPoly);
            }
        }
        }
    }
}

       return map;
    }

    
    
}
