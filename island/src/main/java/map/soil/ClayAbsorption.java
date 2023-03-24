package map.soil;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClayAbsorption implements SoilAbsorption{
    HashMap<Integer, Double> absorption = new HashMap<>();
    public void defineAbsorption(Map island, Structs.Mesh aMesh){
        ArrayList<Structs.Polygon> lakeNeighbours = island.getLakes();
        ArrayList<Structs.Polygon> found = new ArrayList<>();
        double saturation = 100;
        int layers=2;
        List<Integer> neighbours;
        for(int i=0;i<layers;i++) {
            for (Structs.Polygon p : lakeNeighbours) {
                neighbours = p.getNeighborIdxsList();
                for (Integer n : neighbours) {
                    if (!lakeNeighbours.contains(aMesh.getPolygons(n)) && !found.contains(aMesh.getPolygons(n))) {
                        found.add(aMesh.getPolygons(n));
                        absorption.put(n,saturation);
                    }
                }
            }
            saturation/=2;
            lakeNeighbours.addAll(found);
            found.clear();
        }

        for(Structs.Polygon p : island.getLand()){
            if(!lakeNeighbours.contains(p)){
                absorption.put(aMesh.getPolygonsList().indexOf(p), 0.0);
            }
        }

        island.setAbsorption(absorption);
    }
}
