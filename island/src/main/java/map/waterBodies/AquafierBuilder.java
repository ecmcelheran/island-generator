package map.waterBodies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import map.Map;

public class AquafierBuilder implements WaterBuilder{

    @Override
    public Map build(Mesh aMesh, Map map, int numUnits) {
        ArrayList<Structs.Polygon> border = map.getBorder();
        ArrayList<Structs.Polygon> innerLand =  map.getInnerLand();
        Random rand = new Random();
        for(int n=0; n<numUnits; n++){
            Structs.Polygon targetPoly = innerLand.get(rand.nextInt(innerLand.size()));
            map.addAquafTile(targetPoly);
            List<Integer> neighbours = targetPoly.getNeighborIdxsList();
            for(int i: neighbours){
                if(!border.contains(aMesh.getPolygons(i))) {
                    map.addAquafTile(aMesh.getPolygons(i));
                }
            }
        }
       return map;
    }
    
    
}
