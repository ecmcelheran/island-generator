package map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public interface InnerMapBuilder {
    public Map build(Structs.Mesh aMesh, int outerR, Map m);
}
