package map.shape;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

public interface MapBuilder{
    public Map build(Structs.Mesh aMesh, int R, long seed);

}