package elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.HashMap;

public class FlatBuilder implements Elevation{
    private HashMap<Integer, Double> elevation = new HashMap<>();
    public HashMap<Integer, Double> assignElevations(Map island, Structs.Mesh aMesh){

        for (Structs.Polygon p: island.getLand()){
                elevation.put(aMesh.getPolygonsList().indexOf(p), 0.0);
        }
        return elevation;
    }
}
