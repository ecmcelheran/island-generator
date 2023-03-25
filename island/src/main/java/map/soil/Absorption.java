package map.soil;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Absorption implements SoilAbsorption{
    HashMap<Integer, Double> absorption = new HashMap<>();
    private final String SOIL;
    int layers;

    public Absorption(String SOIL){
        this.SOIL = SOIL;
        switch(SOIL) {
            case "clay" -> {
                layers = 2;
            }
            case "silt" -> {
                layers = 3;
            }
            case "sand" -> {
                layers = 5;
            }
        }
    }

    public double updateSaturation(double saturation){
        switch(SOIL){
            case "clay", "silt" ->{
                return saturation/2;
            }
            case "sand"->{
                return saturation/3;
            }
        }
        return saturation;
    }


    public void defineAbsorption(Map island, Structs.Mesh aMesh){
        ArrayList<Structs.Polygon> lakeNeighbours = island.getLakes();
        ArrayList<Structs.Polygon> found = new ArrayList<>();
        double saturation = 100.0;
        List<Integer> neighbours;
        for(int i=0;i<layers;i++) {
            for (Structs.Polygon p : lakeNeighbours) {
                neighbours = p.getNeighborIdxsList();
                for (Integer n : neighbours) {
                    if (!lakeNeighbours.contains(aMesh.getPolygons(n)) && !absorption.containsKey(n)) {
                        found.add(aMesh.getPolygons(n));
                        absorption.put(n,saturation);
                    }
                }
            }
            saturation=updateSaturation(saturation);
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
