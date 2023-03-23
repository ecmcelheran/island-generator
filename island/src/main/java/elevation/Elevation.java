package elevation;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.HashMap;

public interface Elevation {

    public HashMap<Integer,Double> assignElevations(Map island, Structs.Mesh aMesh);
}
