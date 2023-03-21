package map.waterBodies;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

public interface WaterBuilder {

    public Map build(Structs.Mesh aMesh, Map map, int numUnits);
    
}
