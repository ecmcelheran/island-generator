package map.elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PeakBuilder implements Elevation{
    private HashMap<Integer, Double> elevation = new HashMap<>();
    private int num=1;

    public void setNum(int num){
        this.num = num;
    }

    public void assignElevations(Map island, Structs.Mesh aMesh, long seed){
        Random r = new Random();
        r.setSeed(seed);
        Structs.Polygon origin;
        ArrayList<Structs.Polygon> temp = new ArrayList<>();
        ArrayList<Structs.Polygon> peak = new ArrayList<>();
        ArrayList<Structs.Polygon> thisPeak = new ArrayList<>();
        List<Integer> neighbours;
        for(int i=0; i<num; i++) {
            int bound = island.getInnerLand().size();
            if(bound>0){
                origin = island.getInnerLand().get(r.nextInt(0, bound));
            }
            else{
                break;
            }
            elevation.put(aMesh.getPolygonsList().indexOf(origin), 200.0);
            peak.add(origin);
            thisPeak.add(origin);
            double height = 149;
            while (height > 1) {
                for (Structs.Polygon p : thisPeak) {
                    neighbours = p.getNeighborIdxsList();
                    for (Integer n : neighbours) {
                        if(!thisPeak.contains(aMesh.getPolygons(n))&&island.getLand().contains(aMesh.getPolygons(n))&&!temp.contains(aMesh.getPolygons(n))){
                            if (!peak.contains(aMesh.getPolygons(n))){
                                temp.add(aMesh.getPolygons(n));
                                elevation.put(n, height);
                            }
                            else{
                                if(elevation.get(n)<height){
                                    elevation.replace(n,elevation.get(n), height);
                                }
                            }
                        }
                    }
                }
                thisPeak.addAll(temp);
                temp.clear();
                height -= 50;
            }
            peak.addAll(thisPeak);
            thisPeak.clear();
        }
        for(Structs.Polygon p : island.getLand()){
            if(!peak.contains(p)){
                elevation.put(aMesh.getPolygonsList().indexOf(p),0.0);
            }
        }
        island.setElevation(elevation);
    }
    @Override
    public int assignTemp(int elevTemperature) {
        elevTemperature = -7 ;
        return elevTemperature;
    }
}
