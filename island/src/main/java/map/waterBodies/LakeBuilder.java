package map.waterBodies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

public class LakeBuilder implements WaterBuilder {

    @Override
    public Map build(Structs.Mesh aMesh, Map map, int numUnits) {
        //ArrayList<Structs.Polygon> land = map.getLand();
        
        ArrayList<Structs.Polygon> border = map.getBorder();
        ArrayList<Structs.Polygon> innerLand =  map.getInnerLand();

        Random rand = new Random();
        int atLeast = numUnits/2;
        int numLakes = rand.nextInt(atLeast, numUnits);
        for(int n=0; n<numLakes; n++){
            
            Structs.Polygon targetPoly = innerLand.get(rand.nextInt(innerLand.size()));
            map.addLakeTile(targetPoly);
            map.removeLandTile(targetPoly);
            List<Integer> neighbours = targetPoly.getNeighborIdxsList();
            for(int i: neighbours){
                if(!border.contains(aMesh.getPolygons(i)))
                    map.removeLandTile(aMesh.getPolygons(i));
                    map.addLakeTile(aMesh.getPolygons(i));
            }

        }
       return map;
    }

    
    
}
