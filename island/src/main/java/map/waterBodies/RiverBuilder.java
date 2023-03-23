package map.waterBodies;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import map.Map;

public class RiverBuilder implements WaterBuilder{

    @Override
    public Map build(Mesh aMesh, Map map, int numUnits) {

        List<Structs.Polygon> polygons = aMesh.getPolygonsList();
        ArrayList<Structs.Polygon> innerLand = map.getInnerLand();
        HashMap<Integer,Double> elevations = map.getElevation();
        
        //ArrayList<Structs.Segment> river = new ArrayList<>();
        Random rand = new Random();
        List<Integer> neighbours;
        for(int i=0; i<numUnits; i++){
            int index = rand.nextInt(innerLand.size());
            Structs.Polygon targetPoly = innerLand.get(index);
            Structs.Segment springSeg = aMesh.getSegments( targetPoly.getSegmentIdxs(0));
            map.addRiverSegments(springSeg);
            double minElevation =  elevations.get(polygons.indexOf(targetPoly));
            int minNeighbour = targetPoly.getNeighborIdxsList().get(0);
            boolean water = false;
            //while(!water){
                neighbours = targetPoly.getNeighborIdxsList();
                for(int j: neighbours){ // find smallest elevation for each neighbour
                    if(elevations.containsKey(j)){
                        double neighElevation =  elevations.get(j); 
                        if(neighElevation<= minElevation){
                            minNeighbour = j;
                        }
                    }else{
                        water = true;
                    }
                }
                targetPoly = polygons.get(minNeighbour);
                if(!innerLand.contains(targetPoly)){// IF WE HIT WATER
                    water = true; 
                }
                map.addRiverSegments(aMesh.getSegments(targetPoly.getSegmentIdxs(0))); 
            //}
        }
        System.out.println("num rivers: "+map.getRivers().size());
        return map;
    }
    
    
}
