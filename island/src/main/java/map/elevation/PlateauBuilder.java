package map.elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlateauBuilder implements Elevation{
    private HashMap<Integer, Double> elevation = new HashMap<>();
    public int elevTemperature = 5;
    public void assignElevations(Map island, Structs.Mesh aMesh, long seed) {
        Random r = new Random();
        r.setSeed(seed);
        ArrayList<Structs.Polygon> plateau = new ArrayList<>();
        double plateauHeight = r.nextInt(25, 180);
        int bound = island.getInnerLand().size();
        Structs.Polygon origin;
        boolean cont = false;
        if(bound>0){
            origin = island.getInnerLand().get(r.nextInt(0, bound));
            plateau.add(origin);
            cont = true;
        }
        List<Integer> neighbours;
        ArrayList<Structs.Polygon> toAdd = new ArrayList<>();
        while (cont) {
            for (Structs.Polygon p : plateau) {
                neighbours = p.getNeighborIdxsList();
                for (Integer n : neighbours) {
                    if (island.getInnerLand().contains(aMesh.getPolygons(n))) {
                        toAdd.add(aMesh.getPolygons(n));
                    } else {
                        cont = false;
                    }
                }
            }
            plateau.addAll(toAdd);
            toAdd.clear();
        }

        //go through all polygons - if part of plateau set elevation to defined height, otherwise elevation is 0
        for (Structs.Polygon p: island.getLand()){
            if(plateau.contains(p)){
                elevation.put(aMesh.getPolygonsList().indexOf(p),plateauHeight);
            }
            else {
                elevation.put(aMesh.getPolygonsList().indexOf(p), 0.0);
            }
        }
        island.setElevation(elevation);
    }

    public int getTemp() {
        return elevTemperature;
    }
}
