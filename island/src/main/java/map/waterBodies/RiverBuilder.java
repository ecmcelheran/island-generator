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
            ArrayList<Integer> river = new ArrayList<>();
            river.add(targetPoly.getCentroidIdx());
            double compareElevation =  elevations.get(polygons.indexOf(targetPoly));
            boolean water=false;
            while(!water){
                neighbours = targetPoly.getNeighborIdxsList();
                int minNeighbour = 0;
                for(int j: neighbours){ // find smallest elevation for each neighbour
                    if(map.getOcean().contains(polygons.get(j))||map.getLakes().contains(polygons.get(j))){
                        water = true;
                        minNeighbour = j;
                        break;
                    }
                    else if(elevations.containsKey(j)){
                        double neighElevation =  elevations.get(j); 
                        if(neighElevation<= compareElevation){
                            minNeighbour = j;
                        }
                    }
                }
                targetPoly = polygons.get(minNeighbour);
                if(!innerLand.contains(targetPoly)){// IF WE HIT WATER
                    water = true; 
                }
                river.add(targetPoly.getCentroidIdx()); 
            }
            map.addRiver(river); 


        }
        System.out.println("num rivers: "+map.getRivers().size());
        return map;
    }
    
    
}
