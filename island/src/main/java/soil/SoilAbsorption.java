package soil;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

public interface SoilAbsorption {
    public void defineAbsorption(Map island, Structs.Mesh aMesh);
}
