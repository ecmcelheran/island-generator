package elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlateauBuilder implements Elevation{
    HashMap<Integer, Double> elevation = new HashMap<>();
    public HashMap<Integer,Double> assignElevations(Map island, Structs.Mesh aMesh) {
        Random r = new Random();
        ArrayList<Structs.Polygon> plateau = new ArrayList<>();
        double plateauHeight = r.nextInt(25, 180);
        Structs.Polygon origin = island.getInnerLand().get(r.nextInt(0, island.getInnerLand().size()));
        plateau.add(origin);
        List<Integer> neighbours;
        ArrayList<Structs.Polygon> toAdd = new ArrayList<>();
        boolean cont = true;
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
        return elevation;
    }
}
