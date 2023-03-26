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
    public Map build(Mesh aMesh, Map map, int numUnits, long seed) {


        List<Structs.Polygon> polygons = aMesh.getPolygonsList();
        ArrayList<Structs.Polygon> innerLand = map.getInnerLand();
        ArrayList<Structs.Polygon> land = map.getLand();
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
                    if(!land.contains(polygons.get(j))){
                        water = true;
                        minNeighbour = j;
                        List<Integer> targetSeg = targetPoly.getSegmentIdxsList();
                        List<Integer> neighSeg = polygons.get(j).getSegmentIdxsList();
                        for(int k: targetSeg){
                            for(int l: neighSeg){
                                if(k==l){
                                    minNeighbour = aMesh.getSegments(k).getV1Idx();
                                }
                            }
                            
                        }
                        break;
                    }
                    else if(elevations.containsKey(j)){
                        double neighElevation =  elevations.get(j); 
                        if(neighElevation <= compareElevation && !river.contains(polygons.get(j).getCentroidIdx())){
                            minNeighbour = j;
                        }
                    }
                }
                if(water){
                    river.add(minNeighbour);
                }else{
                    targetPoly = polygons.get(minNeighbour);
                    river.add(targetPoly.getCentroidIdx()); 
                }
            }
            map.addRiver(river); 


        }
        return map;
    }

      
    
}
