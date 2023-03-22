package elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlateauBuilder implements Elevation{
    public HashMap<Integer,Double> assignElevations(Map island, Structs.Mesh aMesh){
        Random r = new Random();
        HashMap<Integer, Double> elevation = new HashMap<>();
        ArrayList<Structs.Polygon> plateau = new ArrayList<>();
        int plateauHeight = r.nextInt(25,200);
        //define polygons in plateau
        //go through all polygons - if part of plateau set elevation to defined height, otherwise elevation is 0
        Structs.Polygon origin = island.getInnerLand().get(r.nextInt(0,island.getInnerLand().size()));
        plateau.add(origin);
        List<Integer> neighbours;
        boolean cont = true;
        while(cont) {
            for (Structs.Polygon p : plateau) {
                neighbours = p.getNeighborIdxsList();
                for (Integer n : neighbours) {
                    if (island.getInnerLand().contains(aMesh.getPolygons(n))) {
                        plateau.add(aMesh.getPolygons(n));
                    } else {
                        cont = false;
                    }
                }
            }
        }
        return elevation;
    }
}
